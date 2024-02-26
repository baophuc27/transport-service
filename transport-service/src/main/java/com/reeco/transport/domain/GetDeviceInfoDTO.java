package com.reeco.transport.domain;

import lombok.Data;

@Data
public class GetDeviceInfoDTO {
    String id;

    @Override
    public String toString() {
        return "GetDeviceInfoDTO{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
