package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IndicatorMapper extends DomainEntityMapper<Indicator, IndicatorInfo> {

}
