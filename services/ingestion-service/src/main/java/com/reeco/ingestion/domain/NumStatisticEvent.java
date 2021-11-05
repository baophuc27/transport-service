package com.reeco.ingestion.domain;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;


public class NumStatisticEvent {

    private Long organizationId;

    private String date;

    private Long paramId;

    private Double min;

    private Double max;

    private Double mean;

    private Double acc;

    private Long count;

    LocalDateTime lastUpdated;
}
