package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrgAndParam {
    private Long organizationId;

    private Long paramId;

    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrgAndParam)) return false;
        OrgAndParam that = (OrgAndParam) o;
        return Objects.equals(getOrganizationId(), that.getOrganizationId()) &&
                Objects.equals(getParamId(), that.getParamId()) &&
                Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrganizationId(), getParamId(), getDate());
    }
}
