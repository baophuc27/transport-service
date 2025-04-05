package com.reeco.config.service;

import com.reeco.common.model.dto.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface ParameterService {
    DeferredResult<ResponseEntity<String>> createParameter(Parameter parameter);
    DeferredResult<ResponseEntity<String>> deleteParameter(Long id, Long connectionId, Long orgId);
}