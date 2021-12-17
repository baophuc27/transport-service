package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.TransportType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HTTPConnection {
    @NotNull(message = "id must be not NULL")
    private Long id;

    @NotNull(message = "workspaceId must be not NULL")
    private Long workspaceId;

    @NotNull(message = "organizationId must be not NULL")
    private Long organizationId;

    private Long stationId;

    private String accessToken;

    @NotNull(message = "englishName must be not NULL")
    @NotBlank(message = "englishName must be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName must be not NULL")
    @NotBlank(message = "vietnameseName must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "HTTP|FTP", message = "parameterType must be in {HTTP, FPT}")
    private TransportType transportType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public HTTPConnection(){}
}
