package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface NumericalTsEventMapper extends DomainEntityMapper<NumericalTsEvent, NumericalTsByOrg> {
}
