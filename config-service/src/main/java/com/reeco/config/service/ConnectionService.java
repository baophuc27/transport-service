package com.reeco.config.service;

import com.reeco.common.model.dto.FTPConnection;
import com.reeco.common.model.dto.HTTPConnection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

public interface ConnectionService {
    DeferredResult<ResponseEntity<String>> createConnection(String protocol, Map<String, Object> connectionPayload);
    DeferredResult<ResponseEntity<String>> deleteFtpConnection(Long id, Long orgId);
    DeferredResult<ResponseEntity<String>> deleteHttpConnection(Long id, Long orgId);
}