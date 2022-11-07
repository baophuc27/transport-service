package com.reeco.transport.domain;

import com.reeco.common.model.enumtype.ParamType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DetailSharingDataDTO {
    Long userId;

    @NotNull
    Long requestId;

    Long connectionId;

    Long organizationId;

    Long workspaceId;

    Long stationId;

    ParamType parameterType;

    DetailSharingDataParamDTO[] params;
}
