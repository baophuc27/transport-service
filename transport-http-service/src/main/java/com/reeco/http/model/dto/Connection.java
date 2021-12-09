package com.reeco.http.model.dto;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.model.entity.ConnectionByOrg;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Connection {

    private Long orgId;

    private Long connectionId;

    private Long stationId;

    private String accessToken;

    private String name;

    private TransportType transportType;

    private List<Parameter> parameterList = new ArrayList<>();

    public Connection(ConnectionByOrg connectionByOrg){
        this.orgId = connectionByOrg.getPartitionKey().getOrganizationId();
        this.connectionId = connectionByOrg.getPartitionKey().getConnectionId();
        this.accessToken = connectionByOrg.getAccessToken();
        this.name = connectionByOrg.getName();
        this.transportType = connectionByOrg.getTransportType();
    }
}
