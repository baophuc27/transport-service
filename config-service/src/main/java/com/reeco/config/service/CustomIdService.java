package com.reeco.config.service;

import com.reeco.common.model.dto.CustomId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface CustomIdService {
    DeferredResult<ResponseEntity<String>> createCustomId(CustomId customIdPayload);
    DeferredResult<ResponseEntity<String>> deleteCustomId(String idType, Integer originalId);
}