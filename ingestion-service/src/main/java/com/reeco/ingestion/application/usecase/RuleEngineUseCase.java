package com.reeco.ingestion.application.usecase;


import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.dto.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.domain.Indicator;

public interface RuleEngineUseCase {

    boolean checkThreshold(IncomingTsEvent event);

    boolean checkRange(IncomingTsEvent event);

    boolean handleRuleEvent(Alarm alarm, IncomingTsEvent event, Indicator indicator);

    boolean checkMatchingAlarmCondition(Alarm alarm, Double value);

    boolean checkMatchingAlarmCondition(Alarm alarm, String value);
}
