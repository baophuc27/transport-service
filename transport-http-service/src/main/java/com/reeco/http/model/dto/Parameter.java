package com.reeco.http.model.dto;

import com.reeco.http.model.entity.ParamsByOrg;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Parameter {

    private Long orgId;

    private Long paramId;

    private Long connectionId;

    private String paramName;

    private Long stationId;

    private Long workspaceId;

    private String indicatorName;

    private Long indicatorId;


    public Parameter(ParamsByOrg paramsByOrg){
        this.paramId = paramsByOrg.getPartitionKey().getParamId();
        this.orgId = paramsByOrg.getPartitionKey().getOrganizationId();
        this.connectionId = paramsByOrg.getConnectionId();
        this.paramName = paramsByOrg.getParamName();
        this.stationId = paramsByOrg.getStationId();
        this.workspaceId = paramsByOrg.getStationId();
        this.indicatorId = paramsByOrg.getIndicatorId();
    }
}
