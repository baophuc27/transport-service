package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    LocalDateTime lastUpdated;


}
