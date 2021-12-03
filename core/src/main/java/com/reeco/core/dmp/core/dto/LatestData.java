package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class LatestData {
    private  Long organizationId;

    private List<LatestDataConnection> connectionList;
}
