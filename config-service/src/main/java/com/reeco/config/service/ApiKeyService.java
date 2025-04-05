package com.reeco.config.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

public interface ApiKeyService {
    DeferredResult<ResponseEntity<String>> upsertAPIKey(Map<String, Object> apiKeyPayload);
    DeferredResult<ResponseEntity<String>> deleteApiKey(String id);
    DeferredResult<ResponseEntity<String>> upsertMQTTShare(Map<String, Object> mqttPayload);
    DeferredResult<ResponseEntity<String>> deleteMqttShare(String id);
}