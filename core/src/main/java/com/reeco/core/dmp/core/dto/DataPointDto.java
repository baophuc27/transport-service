package com.reeco.core.dmp.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.reeco.core.dmp.core.model.CategoricalStatByOrg;
import com.reeco.core.dmp.core.model.CategoricalTsByOrg;
import com.reeco.core.dmp.core.model.NumericalStatByOrg;
import com.reeco.core.dmp.core.model.NumericalTsByOrg;
import lombok.*;

import java.time.LocalDateTime;

//@Data
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPointDto {
    private String value;

    private LocalDateTime eventTime;

    private Boolean isAlarm = Boolean.FALSE;

    private String alarmType;

    private Long alarmId;

    private Long count;
    private String max;
    private String min;
    private String mean;
    private String median;
    private String sum;
    private String range;
    private String start;
    private String end;
    private String delta;
    private String interpolated;

//    private String etime;

//    private Long connectionId;

//    private Long stationId;

    private Double lat;

    private Double lon;

    public DataPointDto(NumericalTsByOrg num){
        this.value = String.valueOf(num.getValue());
        this.eventTime = num.getPartitionKey().getEventTime();
//        this.etime = num.getPartitionKey().getEventTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        if (num.getLat() != null){
            this.lat = num.getLat();
        }
        if (num.getLon()!=null){
            this.lon = num.getLon();
        }
        if(num.getIsAlarm()!=null){
            this.isAlarm = num.getIsAlarm();
        }
        if(num.getAlarmType()!=null){
            this.alarmType = num.getAlarmType();
        }
        if(num.getAlarmId()!=null){
            this.alarmId = num.getAlarmId();
        }
    }

    public DataPointDto(CategoricalTsByOrg categoricalTsByOrg){
        this.value = categoricalTsByOrg.getPartitionKey().getValue();
        this.eventTime = categoricalTsByOrg.getPartitionKey().getEventTime();
        if(categoricalTsByOrg.getIsAlarm()!=null) {
            this.isAlarm = categoricalTsByOrg.getIsAlarm();
        }
        if (categoricalTsByOrg.getLat() != null){
            this.lat = categoricalTsByOrg.getLat();
        }
        if (categoricalTsByOrg.getLon()!=null){
            this.lon = categoricalTsByOrg.getLon();
        }
        if(categoricalTsByOrg.getAlarmType()!=null){
            this.alarmType = categoricalTsByOrg.getAlarmType();
        }
        if(categoricalTsByOrg.getAlarmId()!=null){
            this.alarmId = categoricalTsByOrg.getAlarmId();
        }
    }

    public DataPointDto(NumericalStatByOrg numericalStatByOrg){
        this.value = numericalStatByOrg.getMean().toString();
        this.max = numericalStatByOrg.getMax().toString();
        this.min = numericalStatByOrg.getMin().toString();
        this.median = numericalStatByOrg.getMedian().toString();
        this.mean = numericalStatByOrg.getMean().toString();
        this.eventTime = numericalStatByOrg.getPartitionKey().getDate().atStartOfDay();
    }

    public DataPointDto(CategoricalStatByOrg categoricalStatByOrg){
        this.value = categoricalStatByOrg.getPartitionKey().getValue();
        this.count = categoricalStatByOrg.getValueCount();
        this.eventTime = categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay();
    }
}
