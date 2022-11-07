package com.reeco.ingestion.application.usecase;


import com.reeco.common.model.dto.AlarmMessage;

public interface ManageAlarmMessageUseCase {

    void storeAlarmMessage(AlarmMessage alarmMessage);
}
