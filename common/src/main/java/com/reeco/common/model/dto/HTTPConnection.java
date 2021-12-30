package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HTTPConnection extends BaseConnection {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public HTTPConnection(){}

    public HTTPConnection(Long id, Long organizationId, Long stationId, Long workspaceId, String englishName, String vietnameseName, Boolean active, Protocol protocol) {
        super(id, organizationId, stationId, workspaceId, englishName, vietnameseName, active, protocol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HTTPConnection)) return false;
        if (!super.equals(o)) return false;
        HTTPConnection that = (HTTPConnection) o;
        return Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUpdatedAt());
    }
}
