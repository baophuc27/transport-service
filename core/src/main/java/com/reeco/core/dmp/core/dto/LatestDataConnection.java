package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class LatestDataConnection {
    private Long connectionId;

    private LocalDateTime latestTime;
}
