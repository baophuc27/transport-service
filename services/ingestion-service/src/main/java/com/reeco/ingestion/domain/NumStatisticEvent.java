package com.reeco.ingestion.domain;

import com.datastax.oss.driver.shaded.guava.common.math.StatsAccumulator;
import com.reeco.ingestion.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class NumStatisticEvent {

    private Long organizationId;

    private Long paramId;

    private LocalDate date;

    private Double min;

    private Double max;

    private Double mean;

    private Double acc;

    private Long count;

    LocalDateTime lastUpdated;

    public NumStatisticEvent(Long organizationId, Long paramId) {
        this.organizationId = organizationId;
        this.paramId = paramId;
    }


}
