package com.reeco.ingestion.infrastructure.model;


/*
* Just need a plain java class that include enough information
* It's not necessary to implement validation in adapter.
* Instead, we should validate that:
* WE CAN TRANSFORM THE INPUT MODEL OF THE ADAPTER INTO
* THE INPUT MODEL OF THE USE CASES. IF NOT, WE'LL THROW VALIDATION ERROR.
* */

import lombok.Data;

@Data
public class RegisterDeviceResource {

    private String deviceId;

    private String deviceProtocol;

}
