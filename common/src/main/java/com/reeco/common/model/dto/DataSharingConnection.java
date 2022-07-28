package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSharingConnection extends BaseConnection{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public DataSharingConnection(){}

    public DataSharingConnection(Long id, Long organizationId, Long stationId, Long workspaceId, String englishName, String vietnameseName, Boolean active, Protocol protocol) {
        super(id, organizationId, stationId, workspaceId, englishName, vietnameseName, active, protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUpdatedAt());
    }
}
