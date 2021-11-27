package com.reeco.core.dmp.core.dto;

import com.reeco.core.dmp.core.model.CategoricalStatByOrg;
import com.reeco.core.dmp.core.model.CategoricalTsByOrg;
import com.reeco.core.dmp.core.model.NumericalStatByOrg;
import com.reeco.core.dmp.core.model.NumericalTsByOrg;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class DataPointDto {
    private String value;

    private LocalDateTime eventTime;

    private Boolean isAlarm = Boolean.FALSE;

    private String alarmType;

//    private String etime;

//    private Long connectionId;

//    private Long stationId;

    private Double lat;

    private Double lon;

    public DataPointDto(NumericalTsByOrg num){
        this.value = num.getValue().toString();
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
    }

    public  DataPointDto(NumericalStatByOrg numericalStatByOrg){
        this.value = numericalStatByOrg.getMean().toString();
        this.eventTime = numericalStatByOrg.getPartitionKey().getDate().atStartOfDay();
    }

    public  DataPointDto(CategoricalStatByOrg categoricalStatByOrg){
        this.value = categoricalStatByOrg.getPartitionKey().getValue();
        this.eventTime = categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay();
    }
}
