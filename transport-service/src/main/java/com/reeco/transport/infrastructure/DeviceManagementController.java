package com.reeco.transport.infrastructure;

import com.reeco.transport.utils.annotators.Infrastructure;
import com.reeco.transport.infrastructure.model.RegisterDeviceResource;
import com.reeco.transport.application.usecase.DeviceManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Infrastructure
@RestController
@RequestMapping(path="/device")
@RequiredArgsConstructor
class DeviceManagementController {

    private final DeviceManagementUseCase deviceManagementUseCase;

    @PostMapping(value="/register",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public boolean registerDevice(@RequestBody RegisterDeviceResource registerDeviceResource) {
        try {
//            RegisterDeviceCommand command = new RegisterDeviceCommand(
//                    registerDeviceResource.getDeviceId(),
//                    registerDeviceResource.getDeviceProtocol());
//
//            return deviceManagementUseCase.registerDevice(command);
            return true;
        }
        catch (RuntimeException ex){
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Some error messages.");
        }
    }


}
