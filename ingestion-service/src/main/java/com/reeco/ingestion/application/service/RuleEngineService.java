package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.MaintainType;
import com.reeco.common.model.enumtype.ValueType;
import com.reeco.common.model.dto.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.application.port.out.AlarmEvent;
import com.reeco.ingestion.application.usecase.RuleEngineUseCase;
import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.IndicatorCacheUseCase;
import com.reeco.ingestion.cache.service.RuleEngineCacheUseCase;
import com.reeco.ingestion.domain.Indicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.abs;

@RequiredArgsConstructor
@Log4j2
public class RuleEngineService implements RuleEngineUseCase {

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    IndicatorCacheUseCase indicatorCacheUseCase;

    @Autowired
    RuleEngineCacheUseCase ruleEngineCacheUseCase;

    @Autowired
    KafkaTemplate<String, AlarmEvent> alarmEventTemplate;

    private final String ALARM_RULE_TOPIC = "reeco_alarm_noti_event";

    private final String ALARM_TOPIC = "reeco_alarm_event";

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

    private boolean isMatchCount(Alarm alarm, AlarmRuleCache alarmRuleCache) {
        return alarmRuleCache.getMatchedCount().equals(alarm.getNumOfMatch());
    }

    private boolean isOutOfTimeRange(Alarm alarm,
                                     AlarmRuleCache alarmRuleCache,
                                     IncomingTsEvent event) {
        long minutes = ChronoUnit.MINUTES.between(event.getEventTime(), alarmRuleCache.getLastMatchedTime());
        return abs(minutes) >= abs(alarm.getFrequence() * alarm.getFrequenceType().getValueFromEnum());
    }

    public static boolean isInBracketRange(Alarm alarm, Double value) {
        return ((alarm.getMinValue()==null) || value > Double.parseDouble(alarm.getMinValue()))
                && ((alarm.getMaxValue()==null) || value < Double.parseDouble(alarm.getMaxValue()));
    }
    public static boolean isInSquareRange(Alarm alarm, Double value) {
        return  ((alarm.getMinValue()==null) || value >= Double.parseDouble(alarm.getMinValue()))
                &&((alarm.getMaxValue()==null) || value <= Double.parseDouble(alarm.getMaxValue()));
    }


    private boolean isExactEqualNumber(Alarm alarm, Double value) {
        return value.equals(Double.valueOf(alarm.getMinValue()));
    }

    private boolean isExactEqualCategorical(Alarm alarm, String value) {
        return value.equals(alarm.getMinValue());
    }

    public boolean checkMatchingAlarmCondition(Alarm alarm, IncomingTsEvent event, ValueType valueType) {
        if (alarm.getAlarmType() == AlarmType.THRESHOLD) {
            if (valueType == ValueType.NUMBER) {
                return isExactEqualNumber(alarm, Double.valueOf(event.getValue()));
            } else return isExactEqualCategorical(alarm, event.getValue());
        } else if (alarm.getAlarmType() == AlarmType.SQUARE_RANGE) {
            return isInSquareRange(alarm, Double.valueOf(event.getValue()));
        } else if (alarm.getAlarmType() == AlarmType.BRACKET_RANGE) {
            return isInBracketRange(alarm, Double.valueOf(event.getValue()));
        }
        return false;
    }


    public boolean checkMatchingAlarmCondition(Alarm alarm, Double value) {
        if (alarm.getAlarmType() == AlarmType.THRESHOLD) {
            return isExactEqualNumber(alarm, value);
        } else if (alarm.getAlarmType() == AlarmType.SQUARE_RANGE) {
            return isInSquareRange(alarm, value);
        } else if (alarm.getAlarmType() == AlarmType.BRACKET_RANGE) {
            return isInBracketRange(alarm, value);
        }
        return false;
    }

    public boolean checkMatchingAlarmCondition(Alarm alarm, String value) {
        if (alarm.getAlarmType() == AlarmType.THRESHOLD) {
            return isExactEqualCategorical(alarm, value);
        }
        return false;
    }

    public boolean handleRuleEvent(Alarm alarm, IncomingTsEvent event, Indicator indicator) {
        AlarmRuleCache alarmRuleCache = ruleEngineCacheUseCase.get(alarm.getId().toString());
        boolean isMatchCondition = checkMatchingAlarmCondition(alarm, event, indicator.getValueType());
        MaintainType maintainType = alarm.getMaintainType();
        boolean isAlarmEvent = false;
        if (isMatchCondition) {
            isAlarmEvent = true;
            alarmRuleCache.setMatchedCount(alarmRuleCache.getMatchedCount() + 1);
            boolean isOutOfMatch = isOutOfMatchCount(alarm, alarmRuleCache);
            AlarmEvent alarmEvent = new AlarmEvent(
                    event.getOrganizationId(),
                    event.getWorkspaceId(),
                    event.getStationId(),
                    event.getConnectionId(),
                    event.getParamId(),
                    alarm.getId(),
                    event.getEventTime(),
                    LocalDateTime.now(),
                    event.getValue(),
                    true
            );
            switch (maintainType) {
                case NONE:
                    break;
                case FIRST_TIME_ONLY: {
//                    if (isOutOfMatch) {
                    if (isMatchCount(alarm, alarmRuleCache)) {
                        alarmEventTemplate.send(ALARM_RULE_TOPIC, alarmEvent);
                        log.info("Sent Alarm {} as matched rule {}", alarmEvent, maintainType.name());
                        alarmRuleCache.setIsLastEventRaised(true);
                    }
                    break;
                }
                case MAINTAIN: {
                    if (isMatchCount(alarm, alarmRuleCache) || (isOutOfMatch && isOutOfTimeRange(alarm, alarmRuleCache, event))) {
                        // update Last Matched Time
                        alarmRuleCache.setLastMatchedTime(event.getEventTime());
                        alarmEventTemplate.send(ALARM_RULE_TOPIC, alarmEvent);
                        log.info("Sent Alarm {} as matched rule {}", alarmEvent, maintainType.name());
                        alarmRuleCache.setIsLastEventRaised(true);
                    } else {
                        alarmRuleCache.setIsLastEventRaised(false);
                    }
                    break;
                }
            }
            alarmEventTemplate.send(ALARM_TOPIC, alarmEvent);
            log.info("Sent Alarm {} as matched condition", alarmEvent);
        } else {
            Boolean isLastEventRaised = alarmRuleCache.getIsLastEventRaised();
            if (isLastEventRaised) {
                AlarmEvent alarmEvent = new AlarmEvent(
                        event.getOrganizationId(),
                        event.getWorkspaceId(),
                        event.getStationId(),
                        event.getConnectionId(),
                        event.getParamId(),
                        alarm.getId(),
                        event.getEventTime(),
                        LocalDateTime.now(),
                        event.getValue(),
                        false
                );
                alarmEventTemplate.send(ALARM_RULE_TOPIC, alarmEvent);
                log.info("Sent Alarm {} as no alarm raised (isAlarm = False)", alarmEvent);
            }
            alarmRuleCache.setMatchedCount(0L);
            alarmRuleCache.setLastMatchedTime(event.getReceivedAt());
            alarmRuleCache.setIsLastEventRaised(false);
        }
        ruleEngineCacheUseCase.put(alarmRuleCache);
        return isAlarmEvent;
    }

}
