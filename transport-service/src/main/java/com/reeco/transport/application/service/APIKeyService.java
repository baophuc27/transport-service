package com.reeco.transport.application.service;


import com.reeco.common.model.dto.APIKey;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresApiKeyRepository;
import com.reeco.transport.utils.annotators.Infrastructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Infrastructure
@RequiredArgsConstructor
@Slf4j
@Service
public class APIKeyService {


    private final PostgresApiKeyRepository apiKeyRepository;

    public APIKey getAPIKeyScope(String apiKeyStr){

    }
}
