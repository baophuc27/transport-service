package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ParamsAndOrg {
    private Long organizationId;
    private List<Long> paramsIds;

    @Setter
    private Long maxDate;

    public ParamsAndOrg(Long organizationId, List<Long> paramsId) {
        this.organizationId = organizationId;
        this.paramsIds = paramsId;
    }

    @Override
    public String toString() {
        return "ParamsByOrg{" +
                "organizationId=" + organizationId +
                ", paramsId=" + paramsIds +
                '}';
    }
}