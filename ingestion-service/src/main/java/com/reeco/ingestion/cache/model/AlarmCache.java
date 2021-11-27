package com.reeco.ingestion.cache.model;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.FrequenceType;
import com.reeco.common.model.enumtype.MaintainType;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmCache {
    private String key;

    private Long alarmId;

    private Long orgId;

    private Long paramId;

    private AlarmType alarmType;

    private MaintainType maintainType;

    private FrequenceType frequenceType;

    private String minValue;

    private String maxValue;

    private Long numOfMatch;

    private Long frequence;

    public AlarmCache(AlarmInfo alarmInfo){
        this.alarmId = alarmInfo.getPartitionKey().getAlarmId();
        this.orgId = alarmInfo.getPartitionKey().getOrganizationId();
        this.paramId = alarmInfo.getPartitionKey().getParamId();
        this.key = this.orgId.toString()+"-"+this.paramId.toString()+"-"+this.alarmId.toString();
        this.alarmType = alarmInfo.getAlarmType();
        this.minValue = alarmInfo.getMinValue();
        this.maxValue = alarmInfo.getMaxValue();
        this.maintainType = alarmInfo.getMaintainType();
        this.numOfMatch = alarmInfo.getNumOfMatch();
        this.frequence = alarmInfo.getFrequence();
        this.frequenceType = alarmInfo.getFrequenceType();
    }

    public AlarmCache(Alarm alarm, Long orgId, Long paramId){
        this.alarmId = alarm.getId();
        this.orgId = orgId;
        this.paramId = paramId;
        this.key = this.orgId.toString()+"-"+this.paramId.toString()+"-"+this.alarmId.toString();
        this.alarmType = alarm.getAlarmType();
        this.minValue = alarm.getMinValue();
        this.maxValue = alarm.getMaxValue();
        this.maintainType = alarm.getMaintainType();
        this.numOfMatch = alarm.getNumOfMatch();
        this.frequence = alarm.getFrequence();
        this.frequenceType = alarm.getFrequenceType();
    }
}
