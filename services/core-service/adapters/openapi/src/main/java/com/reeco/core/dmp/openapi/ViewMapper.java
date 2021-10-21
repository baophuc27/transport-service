package com.reeco.core.dmp.openapi;

import com.reeco.core.dmp.application.domain.governance.View;
import com.reeco.shares.api.dmp.view.ViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ViewMapper {

    @Mappings({ @Mapping(target = "serviceAddress", ignore = true), })
    ViewDto entityToApi(View entity);

    @Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "name", ignore = true), })
    View apiToEntity(ViewDto api);
}
