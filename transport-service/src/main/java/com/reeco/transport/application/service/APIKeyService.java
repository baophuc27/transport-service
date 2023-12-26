package com.reeco.transport.application.service;


import com.reeco.common.model.dto.APIKey;
import com.reeco.transport.application.mapper.ApiKeyMapper;
import com.reeco.transport.domain.GetHistoricDataDTO;
import com.reeco.transport.infrastructure.persistence.postgresql.ApiKeyEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresApiKeyRepository;
import com.reeco.transport.utils.annotators.Infrastructure;
import com.reeco.transport.utils.exception.AuthenticationFailedException;
import com.reeco.transport.utils.exception.NotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.thirdparty.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

@Infrastructure
@RequiredArgsConstructor
@Slf4j
@Service
public class APIKeyService {

    private final PostgresApiKeyRepository apiKeyRepository;

    public ApiKeyEntity getAPIKeyScope(String apiKeyStr, GetHistoricDataDTO dataDTO) throws AuthenticationFailedException, NotPermittedException {
        log.info(dataDTO.toString());
        log.info("Query for API Key: {}",apiKeyStr);
        ApiKeyEntity apiKeyEntity = apiKeyRepository.getApiKeyEntitybyKey(apiKeyStr);
        if (apiKeyEntity == null){
            throw new AuthenticationFailedException("Invalid key");
        }

        boolean isPermittedAccess = isPermittedToAccessResource(apiKeyEntity,dataDTO.getId());

        if (!isPermittedAccess){
            throw new NotPermittedException("This API Key can not access your requested resource.");
        }

        return apiKeyEntity;
    }

    private boolean isPermittedToAccessResource(ApiKeyEntity apiKeyEntity, String connectionId){
        return true;
    }
}
