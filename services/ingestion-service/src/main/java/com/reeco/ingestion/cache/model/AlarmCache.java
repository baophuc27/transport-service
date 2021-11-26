package com.reeco.ingestion.cache.model;

import com.hazelcast.nio.serialization.DataSerializable;
import com.reeco.ingestion.Common.AlarmType;
import com.reeco.ingestion.Common.FrequenceType;
import com.reeco.ingestion.Common.MaintainType;
import com.reeco.ingestion.application.port.in.IncomingAlarm;
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
        this.alarmType = AlarmType.valueOf(alarmInfo.getAlarmType());
        this.minValue = alarmInfo.getMinValue();
        this.maxValue = alarmInfo.getMaxValue();
        this.maintainType = MaintainType.valueOf(alarmInfo.getMaintainType());
        this.numOfMatch = alarmInfo.getNumOfMatch();
        this.frequence = alarmInfo.getFrequency();
        this.frequenceType = FrequenceType.valueOf(alarmInfo.getFrequencyType());
    }

    public AlarmCache(IncomingAlarm incomingAlarm, Long orgId, Long paramId){
        this.alarmId = incomingAlarm.getId();
        this.orgId = orgId;
        this.paramId = paramId;
        this.key = this.orgId.toString()+"-"+this.paramId.toString()+"-"+this.alarmId.toString();
        this.alarmType = AlarmType.valueOf(incomingAlarm.getAlarmType());
        this.minValue = incomingAlarm.getMinValue();
        this.maxValue = incomingAlarm.getMaxValue();
        this.maintainType = MaintainType.valueOf(incomingAlarm.getMaintainType());
        this.numOfMatch = incomingAlarm.getNumOfMatch();
        this.frequence = incomingAlarm.getFrequence();
        this.frequenceType = FrequenceType.valueOf(incomingAlarm.getFrequenceType());
    }
}
