package com.reeco.ingestion.application.port.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class IncomingTsEvent {

    public IncomingTsEvent() {
    }

    Long organizationId;

    Long stationId;

    Long connectionId;

    Long paramId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime eventTime;

    Long indicatorId;

    String indicatorName;

    String paramName;

    String value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime receivedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime sentAt;

    Double lat; // nullable

    Double lon;

}
