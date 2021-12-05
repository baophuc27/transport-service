package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.AlarmType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class NumericalStatEvent {

    private Long organizationId;

    private Long paramId;

    private LocalDate date;

    private Double min;

    private Double max;

    private Double mean;

    private Double acc;

    private Double median;

    private Long count;

    private Double std;

    private Boolean isAlarm;

    private Long alarmId;

    private AlarmType alarmType;

    private String minValue;

    private String maxValue;

    LocalDateTime lastUpdated;

}
