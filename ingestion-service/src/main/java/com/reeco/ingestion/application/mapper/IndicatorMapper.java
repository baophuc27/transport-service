package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IndicatorMapper extends DomainEntityMapper<Indicator, IndicatorInfo> {
    @Mappings({
            @Mapping(source = "partitionKey.indicatorId", target = "indicatorId"),
            @Mapping(source = "groupId", target = "groupId"),
            @Mapping(source = "indicatorNameVi", target = "indicatorNameVi"),
            @Mapping(source = "valueType", target = "valueType"),
            @Mapping(source = "standardUnit", target = "standardUnit"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")

    })
    Indicator toDomain(IndicatorInfo indicatorInfo);

    @Mappings({
            @Mapping(source = "indicatorId", target = "partitionKey.indicatorId"),
            @Mapping(source = "groupId", target = "groupId"),
            @Mapping(source = "indicatorNameVi", target = "indicatorNameVi"),
            @Mapping(source = "valueType", target = "valueType"),
            @Mapping(source = "standardUnit", target = "standardUnit"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    IndicatorInfo toPersistence(Indicator indicator);
}
