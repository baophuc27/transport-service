package com.reeco.http.model;

import com.reeco.http.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ParameterCache {

    private Long orgId;

    private Long paramId;

    private Long connectionId;

    private String paramName;

    private Long stationId;

    private Long workspaceId;

    private String indicatorName;

    private Long indicatorId;

    private String token;

    private String paramKey;


    public ParameterCache(ParamsByOrg paramsByOrg){
        this.paramId = paramsByOrg.getPartitionKey().getParamId();
        this.orgId = paramsByOrg.getPartitionKey().getOrganizationId();
        this.connectionId = paramsByOrg.getConnectionId();
        this.paramName = paramsByOrg.getParamName();
        this.stationId = paramsByOrg.getStationId();
        this.workspaceId = paramsByOrg.getStationId();
        this.indicatorId = paramsByOrg.getIndicatorId();
        this.paramKey = paramsByOrg.getKeyName();
    }
}
