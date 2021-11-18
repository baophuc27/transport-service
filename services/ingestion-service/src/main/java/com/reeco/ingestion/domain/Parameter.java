package com.reeco.ingestion.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Parameter {
    private Long organizationId;
    private Long paramId;
    private Long indicatorId;
    private Long stationId;
    private String unit;
    private Long connectionId;
    private String paramName;
    private String indicatorName;
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @Getter
    public static class ParamsByOrg{
        private Long organizationId;
        private List<Long> paramsIds;

        @Setter
        private Long maxDate;

        public ParamsByOrg(Long organizationId, List<Long> paramsId) {
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

}
