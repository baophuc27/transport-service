package com.reeco.shares.api.dmp.view;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter

public class DataPointDto {

    private String value;

    private LocalDateTime eventTime;

    private Long connectionId;

    private Long stationId;

    private Double lat;

    private Double lon;
}
