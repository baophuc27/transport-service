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
public class CategoricalStatEvent {
    private Long organizationId;

    private Long paramId;

    private LocalDate date;

    private Long valueCount;

    private String value;

    LocalDateTime lastUpdated;
}