package com.reeco.http.mapper;

import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.http.infrastructure.persistence.postgresql.entity.HTTPConnectionMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface HTTPConnectionMapper {

    HTTPConnectionMetadata toHttpConnectionMetadata(HTTPConnection httpConnection);

    HTTPConnection toHttpConnection(HTTPConnectionMetadata httpConnectionMetadata);

}
