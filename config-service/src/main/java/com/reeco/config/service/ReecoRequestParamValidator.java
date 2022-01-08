package com.reeco.config.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class ReecoRequestParamValidator<T> {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    public DeferredResult<ResponseEntity<String>> getResponseMessage(T entity) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String l = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",\n"));
            responseWriter.setResult(new ResponseEntity<>(l, HttpStatus.BAD_REQUEST));
            log.error(l);
        }
        return responseWriter;
    }
}
