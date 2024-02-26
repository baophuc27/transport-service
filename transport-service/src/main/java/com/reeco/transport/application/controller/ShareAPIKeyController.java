package com.reeco.transport.application.controller;


import com.reeco.transport.domain.APIKeyDeviceInfoDTO;
import com.reeco.transport.domain.ApiResponse;
import com.reeco.transport.application.service.APIKeyService;
import com.reeco.transport.domain.GetDeviceInfoDTO;
import com.reeco.transport.domain.GetHistoricDataDTO;
import com.reeco.transport.infrastructure.persistence.postgresql.ApiKeyEntity;
import com.reeco.transport.utils.exception.AuthenticationFailedException;
import com.reeco.transport.utils.exception.NotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/api/v1",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RestController
@RequiredArgsConstructor
@Slf4j
public class ShareAPIKeyController {

    @Autowired
    private APIKeyService apiKeyService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "nodes/historic-data",consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity getDataFromKey(@RequestHeader() String token,@RequestBody GetHistoricDataDTO dataDTO){
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        try{
            String output =  apiKeyService.getAPIKeyScope(token,dataDTO);
            return new ResponseEntity<>(output,apiResponse.getStatus());
        }
        catch (AuthenticationFailedException exception){
            apiResponse = ApiResponse.getAuthenticationFailedResponse();
        }
        catch (NotPermittedException exception){
            apiResponse = ApiResponse.getFailureResponse(exception.getMessage());
        }
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity getDevicesInfo(@RequestHeader() String token, @RequestBody(required = false) GetDeviceInfoDTO getDeviceInfoDTO){
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();

        try{
            if (getDeviceInfoDTO == null){
                List<APIKeyDeviceInfoDTO> apiKeyDeviceInfoDTOList = apiKeyService.getAllDeviceInfo(token);
                return new ResponseEntity<>(apiKeyDeviceInfoDTOList,apiResponse.getStatus());
            }
            else {
                APIKeyDeviceInfoDTO apiKeyDeviceInfoDTO = apiKeyService.getOneDeviceInfo(token,getDeviceInfoDTO);
                return new ResponseEntity<>(apiKeyDeviceInfoDTO,apiResponse.getStatus());
            }

        }
        catch (AuthenticationFailedException exception){
            apiResponse = ApiResponse.getAuthenticationFailedResponse();
        }
        catch (NotPermittedException exception){
            apiResponse = ApiResponse.getFailureResponse(exception.getMessage());
        }
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

}
