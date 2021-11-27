package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Alarm {

    public enum AlarmType {
        THRESHOLD, SQUARE_RANGE, BRACKET_RANGE
    }

    public enum MaintainType {
        NONE, FISRTTIME, MAINTAIN
    }

    public enum FrequenceType {
        S, M, H, D
    }

    private static final long serialVersionUID = 1L;
    private Long id;

    private String englishName;

    private String vietnameseName;

    private AlarmType alarmType;

    private Double minValue;

    private Double maxValue;

    private MaintainType maintainType;

    private Integer numOfMatch;

    private Long frequence;

    private FrequenceType frequenceType;


    public Alarm(Long id,
                 String englishName,
                 String vietnameseName,
                 AlarmType alarmType,
                 Double minValue,
                 Double maxValue,
                 MaintainType maintainType,
                 Integer numOfMatch,
                 Long frequence,
                 FrequenceType frequenceType) {
        this.id = id;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.alarmType = alarmType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.maintainType = maintainType;
        this.numOfMatch = numOfMatch;
        this.frequence = frequence;
        this.frequenceType = frequenceType;
    }
}
