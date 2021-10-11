package com.reeco.shares.api.dmp.view;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter

public class DataPointDto {

    private String value;

    private Timestamp eventTime;

    private Long connectionId;

    private Long staionId;

    private Double lat;

    private Double lon;
}
