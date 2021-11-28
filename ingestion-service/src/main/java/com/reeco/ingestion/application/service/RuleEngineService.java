package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.MaintainType;
import com.reeco.common.model.enumtype.ValueType;
import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.application.usecase.RuleEngineUseCase;
import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.IndicatorCacheUseCase;
import com.reeco.ingestion.cache.service.RuleEngineCacheUseCase;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.ParamAndAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Log4j2
public class RuleEngineService implements RuleEngineUseCase {

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    IndicatorCacheUseCase indicatorCacheUseCase;

    @Autowired
    RuleEngineCacheUseCase ruleEngineCacheUseCase;

    @Override
    public boolean checkThreshold(IncomingTsEvent event) {
        return true;
    }

    @Override
    public boolean checkRange(IncomingTsEvent event) {
        return true;
    }

    private boolean isOutOfMatchCount(Alarm alarm, AlarmRuleCache alarmRuleCache) {
        return alarmRuleCache.getMatchedCount() >= alarm.getNumOfMatch();
    }

    private boolean isOutOfTimeRange(Alarm alarm,
                                     AlarmRuleCache alarmRuleCache,
                                     IncomingTsEvent event) {
        long minutes = ChronoUnit.MINUTES.between(event.getEventTime(), alarmRuleCache.getLastMatchedTime());
        return minutes >= alarm.getFrequence();
    }

    private boolean isInSquareRange(Alarm alarm, IncomingTsEvent event) {
        return Double.parseDouble(event.getValue()) >= Double.parseDouble(alarm.getMinValue())
                && Double.parseDouble(event.getValue()) <= Double.parseDouble(alarm.getMaxValue());
    }

    private boolean isInBracketRange(Alarm alarm, IncomingTsEvent event) {
        return Double.parseDouble(event.getValue()) > Double.parseDouble(alarm.getMinValue())
                && Double.parseDouble(event.getValue()) < Double.parseDouble(alarm.getMaxValue());
    }

    private boolean isExactEqualNumber(Alarm alarm, IncomingTsEvent event) {
        return Double.valueOf(event.getValue()).equals(Double.valueOf(alarm.getMinValue()));
    }

    private boolean isExactEqualCategorical(Alarm alarm, IncomingTsEvent event) {
        return event.getValue().equals(alarm.getMinValue());
    }

    public boolean checkMatchingAlarmCondition(Alarm alarm, IncomingTsEvent event, ValueType valueType) {
        if (alarm.getAlarmType() == AlarmType.THRESHOLD) {
            if (valueType == ValueType.NUMBER) {
                return isExactEqualNumber(alarm, event);
            } else return isExactEqualCategorical(alarm, event);
        } else if (alarm.getAlarmType() == AlarmType.SQUARE_RANGE) {
            return isInSquareRange(alarm, event);
        } else if (alarm.getAlarmType() == AlarmType.BRACKET_RANGE) {
            return isInBracketRange(alarm, event);
        }
        return false;
    }

    public RuleEngineEvent handleRuleEvent(Alarm alarm, IncomingTsEvent event, Indicator indicator) {
        AlarmRuleCache alarmRuleCache = ruleEngineCacheUseCase.get(alarm.getId().toString());
        boolean isMatchCondition = checkMatchingAlarmCondition(alarm, event, indicator.getValueType());
        MaintainType maintainType = alarm.getMaintainType();
        boolean isAlarmEvent = false;
        if (isMatchCondition) {
            isAlarmEvent = true;
            alarmRuleCache.setMatchedCount(alarmRuleCache.getMatchedCount() + 1);
            boolean isOutOfMatch = isOutOfMatchCount(alarm, alarmRuleCache);
            switch (maintainType) {
                case NONE:
                    break;
                case FIRST_TIME: {
                    if (isOutOfMatch) {
                        // send alarm message to kafka topic
                        break;
                    }
                }
                case MAINTAIN: {
                    if (isOutOfTimeRange(alarm, alarmRuleCache, event) && isOutOfMatch) {
                        // send alarm message to kafka topic
                        break;
                    }
                }
            }

        } else {
            alarmRuleCache.setMatchedCount(0L);
        }
        ruleEngineCacheUseCase.put(alarmRuleCache);
        return new RuleEngineEvent(
                event.getOrganizationId(),
                event.getStationId(),
                event.getConnectionId(),
                event.getParamId(),
                event.getEventTime(),
                event.getIndicatorId(),
                event.getIndicatorName(),
                event.getParamName(),
                event.getValue(),
                event.getReceivedAt(),
                event.getSentAt(),
                event.getLat(),
                event.getLon(),
                isAlarmEvent,
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getMinValue(),
                alarm.getMaxValue()
        );
    }

}
