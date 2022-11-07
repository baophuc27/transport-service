package com.reeco.transport.application.controller;

import com.reeco.transport.application.service.DataSharingService;
import com.reeco.transport.domain.DetailSharingDataDTO;
import com.reeco.transport.domain.ResponseMessage;
import com.reeco.transport.domain.SharingDataDTO;
import com.reeco.transport.domain.SharingRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "data-sharing", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = { "*" })
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataSharingController {
    @Autowired
    private DataSharingService dataSharingService;


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

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/request",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity createSharingRequest(@RequestBody SharingRequestDTO sharingRequestDTO) {
        String template = dataSharingService.generateCSVTemplate(sharingRequestDTO);
        String csvFileName = "template.csv";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+csvFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(template));
    }


    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<ResponseMessage> receiveDataFile(@ModelAttribute SharingDataDTO sharingDataDTO) {
        String fileName = "";
        try{
            fileName = dataSharingService.receiveData(sharingDataDTO.getCsvFile(),sharingDataDTO.getRequestId(),sharingDataDTO.getUserId());
        } catch (IOException exception) {
            exception.printStackTrace();
            String message = "File duplicated";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
        String response = String.format("File uploaded: %s",fileName);
        return ResponseEntity.ok().body(new ResponseMessage(response));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping(value = "/accept")
    private ResponseEntity<ResponseMessage> acceptDataFile(@RequestBody DetailSharingDataDTO detailSharingDataDTO) {
        dataSharingService.acceptData(detailSharingDataDTO);
        String response = "success";
        return ResponseEntity.ok().body(new ResponseMessage(response));
    }
}

