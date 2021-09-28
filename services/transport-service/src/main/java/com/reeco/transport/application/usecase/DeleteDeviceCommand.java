package com.reeco.transport.application.usecase;

import com.reeco.transport.utils.annotators.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class DeleteDeviceCommand extends SelfValidating<DeleteDeviceCommand> {
    @NotNull int deviceId;

    public DeleteDeviceCommand(int deviceId){
        this.deviceId = deviceId;
        this.validateSelf();
    }
}
