package com.reeco.ingestion.application.port.out;

import com.reeco.common.model.enumtype.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
public class AlarmEvent {

    Long organizationId;

    Long paramId;

    Long alarmId;

    LocalDateTime eventTime;

    LocalDateTime sentAt;

    String value;

}
