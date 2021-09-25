package com.reeco.transport.application.usecase;

import com.reeco.transport.utils.annotators.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class GetDeviceConfigurationQuery extends SelfValidating<GetDeviceConfigurationQuery> {
    @NotNull String deviceId;

    public GetDeviceConfigurationQuery(String deviceId){
        this.deviceId = deviceId;
        this.validateSelf();
    }
}
