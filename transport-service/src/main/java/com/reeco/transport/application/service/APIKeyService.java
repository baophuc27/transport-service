package com.reeco.transport.application.service;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.reeco.common.model.dto.APIKey;
import com.reeco.transport.application.mapper.ApiKeyMapper;
import com.reeco.transport.domain.APIKeyDeviceInfoDTO;
import com.reeco.transport.domain.GetDeviceInfoDTO;
import com.reeco.transport.domain.GetHistoricDataDTO;
import com.reeco.transport.infrastructure.persistence.postgresql.*;
import com.reeco.transport.utils.annotators.Infrastructure;
import com.reeco.transport.utils.exception.AuthenticationFailedException;
import com.reeco.transport.utils.exception.NotPermittedException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Infrastructure
@RequiredArgsConstructor
@Slf4j
@Service
public class APIKeyService {

    private final PostgresApiKeyRepository apiKeyRepository;

    private final PostgresCustomIdsRepository customIdsRepository;

    private final PostgresDeviceRepository deviceRepository;

    private final PostgresAttributeRepository attributeRepository;

    public String getAPIKeyScope(String apiKeyStr, GetHistoricDataDTO dataDTO) throws AuthenticationFailedException, NotPermittedException {
        ApiKeyEntity apiKeyEntity = apiKeyRepository.getApiKeyEntitybyKey(apiKeyStr);
        if (apiKeyEntity == null){
            throw new AuthenticationFailedException("Invalid key");
        }

        boolean isPermittedAccess = isPermittedToAccessResource(apiKeyEntity,dataDTO.getId());

        if (!isPermittedAccess){
            throw new NotPermittedException("This API Key can not access your requested resource.");
        }

        return makeRequest(dataDTO);
    }

    public APIKeyDeviceInfoDTO getOneDeviceInfo(String apiKeyStr, GetDeviceInfoDTO getDeviceInfoDTO) throws AuthenticationFailedException, NotPermittedException{
        ApiKeyEntity apiKeyEntity = apiKeyRepository.getApiKeyEntitybyKey(apiKeyStr);
        if (apiKeyEntity == null){
            throw new AuthenticationFailedException("Invalid key");
        }
        String connectionIds = apiKeyEntity.getConnectionIds();
        Integer deviceId = customIdsRepository.getConnectionIdFromCustomId(getDeviceInfoDTO.getId());

        String[] idsArray = connectionIds.split(",");
        List<String> idsList = Arrays.asList(idsArray);

        if (deviceId == null || !idsList.contains(deviceId.toString())){
            throw new NotPermittedException("This API Key can not access your requested resource.");
        }
        APIKeyDeviceInfoDTO deviceInfoDTO = getDeviceInfoFromId(deviceId);
        return deviceInfoDTO;
    }

    public List<APIKeyDeviceInfoDTO> getAllDeviceInfo(String apiKeyStr) throws AuthenticationFailedException {
        ApiKeyEntity apiKeyEntity = apiKeyRepository.getApiKeyEntitybyKey(apiKeyStr);
        if (apiKeyEntity == null){
            throw new AuthenticationFailedException("Invalid key");
        }
        List<APIKeyDeviceInfoDTO> apiKeyDeviceInfoDTOList = new ArrayList<>();
        log.info(apiKeyEntity.getConnectionIds());
        for (String connectionId : apiKeyEntity.getConnectionIds().split(",")){
            APIKeyDeviceInfoDTO deviceInfoDTO = getDeviceInfoFromId(Integer.valueOf(connectionId));
            apiKeyDeviceInfoDTOList.add(deviceInfoDTO);
        }
        return apiKeyDeviceInfoDTOList;
    }

    private APIKeyDeviceInfoDTO getDeviceInfoFromId(Integer deviceId){
        DeviceEntity device = deviceRepository.findDeviceById(deviceId);
        Boolean isLoggedOut = deviceRepository.findLoggedOutById(deviceId);
        String deviceCustomId = customIdsRepository.getCustomIdFromConnectionId(deviceId);
        APIKeyDeviceInfoDTO apiKeyDeviceInfoDTO = new APIKeyDeviceInfoDTO(
            deviceId.toString(),
                device.getActive(),
                device.getLastActive(),
                device.getEnglishName(),
                deviceCustomId,
                !isLoggedOut
        );
        return apiKeyDeviceInfoDTO;
    }

    private boolean isPermittedToAccessResource(ApiKeyEntity apiKeyEntity, String connectionId){
        return true;
    }

    private String makeRequest(GetHistoricDataDTO getHistoricDataDTO){
        List<RequestParameterDto> requestParameterDtos = getAccessibleParameterFromCustomIds(getHistoricDataDTO.getId());

        int limit = 99999;
        try{
            limit = getHistoricDataDTO.getLimit();
        }
        catch (Exception e){
            log.info("No limit");
        }

        String output = "";
        try {
            URL url = new URL("http://localhost:7001/api/v1/chart/history-data");
            String aggregateType = getHistoricDataDTO.getAggregate() != null ? getHistoricDataDTO.getAggregate() : "NONE";
            String aggregateInterval = getHistoricDataDTO.getAggregateInterval() != null ? getHistoricDataDTO.getAggregateInterval() : "DEFAULT";
            // Create requestBody object
            HistoryDataRequest requestBody = new HistoryDataRequest(
                    getHistoricDataDTO.getStartTime(),
                    getHistoricDataDTO.getEndTime(),
                    aggregateInterval,
                    aggregateType,
                    requestParameterDtos);
            log.info(requestBody.toString());
            // Convert requestBody object to JSON
            Gson gson = new Gson();
            String jsonRequestBody = gson.toJson(requestBody);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method
            connection.setRequestMethod("POST");

            // Set headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Enable output and set request body
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = connection.getResponseCode();
             InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             String line;
             StringBuilder response = new StringBuilder();
             while ((line = reader.readLine()) != null) {
                 response.append(line);
             }

             output = convertJSONFormat(String.valueOf(response),aggregateType,limit);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private String convertJSONFormat(String originalJSON,String aggregateType,int limit){
        // Parse the original JSON
        JsonObject originalJsonObject = new Gson().fromJson(originalJSON, JsonObject.class);

        // Create a new JSON object for the desired format
        JsonObject newJsonObject = new JsonObject();

        // Add docType
        newJsonObject.addProperty("docType", "json");

        // Create header object
        JsonObject header = new JsonObject();
        header.addProperty("startTime", originalJsonObject.get("startTime").getAsString());
        header.addProperty("endTime", originalJsonObject.get("endTime").getAsString());

        // Create columns object
        JsonObject columns = new JsonObject();
        JsonArray parameterDatas = originalJsonObject.getAsJsonArray("parameterDatas");
        Integer count = 0;
        Integer countElement = 0;
        Integer limitEachParameter = 0;
        // Create data array
        JsonArray data = new JsonArray();

        // Add data from parameterDatas

        for (JsonElement parameterData : parameterDatas) {
            JsonObject parameterDataObject = parameterData.getAsJsonObject();
            JsonObject parameterDto = parameterDataObject.getAsJsonObject("parameterDto");
            JsonObject subColumn = new JsonObject();
            subColumn.add("id",parameterDto.get("parameterId"));
            subColumn.add("name",parameterDto.get("parameterName"));
            subColumn.add("dataType",parameterDto.get("indicatorType"));
            subColumn.addProperty("aggregate",aggregateType);
            columns.add(count.toString(),subColumn);

            JsonArray dataPointDtos = parameterDataObject.getAsJsonArray("dataPointDtos");
            for (JsonElement dataPoint : dataPointDtos) {
                countElement += 1;
                limitEachParameter += 1;
                JsonObject newDataPoint = new JsonObject();
                newDataPoint.addProperty("ts", dataPoint.getAsJsonObject().get("eventTime").getAsString());
                JsonObject f = new JsonObject();
                JsonObject dataPointObject = dataPoint.getAsJsonObject();
                String value = dataPointObject.get("value").getAsString();
                JsonObject dataValue = new JsonObject();
                dataValue.addProperty("v",value);
                f.add(count.toString(), dataValue);
                newDataPoint.add("f", f);
                data.add(newDataPoint);
                if (limitEachParameter == limit){
                    limitEachParameter = 0;
                    break;
                }
            }
            count += 1;
        }
        header.addProperty("recordCount", countElement);

        header.add("columns", columns);

        newJsonObject.add("header", header);
        newJsonObject.add("data", data);

        // Convert the new JSON object to a string
        String newJson = new Gson().toJson(newJsonObject);
//        String newJson = "";

        return newJson;
    }

    private List<RequestParameterDto> getAccessibleParameterFromCustomIds(String customIds){
        CustomIdEntity customIdEntity = customIdsRepository.getEntityFromCustomId(customIds);
        List<RequestParameterDto> accessibleParameter = new ArrayList<>();
        if (customIdEntity == null){
            return accessibleParameter;
        }
        String customIdType = customIdEntity.getCustomIdType();
        Integer originalId = customIdEntity.getOriginalId();

        if (Objects.equals(customIdType, "PARAMETER")){
            AttributeEntity attributeEntity = attributeRepository.findParameterById(originalId);
            RequestParameterDto requestParameterDto = mapAttributeEntityToRequestParameterDto(attributeEntity);
            accessibleParameter.add(requestParameterDto);

        } else if (customIdType.equals("CONNECTION")) {
            Collection<AttributeEntity> listAttributeEntity = attributeRepository.findListParameterByConnectionId(originalId);
            for (AttributeEntity attributeEntity : listAttributeEntity){
                RequestParameterDto requestParameterDto = mapAttributeEntityToRequestParameterDto(attributeEntity);
                accessibleParameter.add(requestParameterDto);
            }
        }
        else if (customIdType.equals("ORGANIZATION")) {
            Collection<AttributeEntity> listAttributeEntity = attributeRepository.findListParameterByOrganizationId(originalId);
            for (AttributeEntity attributeEntity : listAttributeEntity){
                RequestParameterDto requestParameterDto = mapAttributeEntityToRequestParameterDto(attributeEntity);
                accessibleParameter.add(requestParameterDto);
            }
        }
        else if (customIdType.equals("STATION")){
            Collection<AttributeEntity> listAttributeEntity = attributeRepository.findListParameterByStationId(originalId);
            for (AttributeEntity attributeEntity : listAttributeEntity){
                RequestParameterDto requestParameterDto = mapAttributeEntityToRequestParameterDto(attributeEntity);
                accessibleParameter.add(requestParameterDto);
            }
        }

        Set<RequestParameterDto> set = new HashSet<>(accessibleParameter);

        // Convert Set back to List
        return new ArrayList<>(set);
    }

    private RequestParameterDto mapAttributeEntityToRequestParameterDto(AttributeEntity attributeEntity){
        int organizationId = attributeEntity.getOrganizationId();
        int parameterId = attributeEntity.getId();
        return new RequestParameterDto((long) organizationId, (long) parameterId);
    }
}

@Data
class HistoryDataRequest {
    private String startTime;
    private String endTime;
    private String resolution;
    private String aggregate;
    private List<RequestParameterDto> parameterDtos;

    public HistoryDataRequest(String startTime, String endTime, String resolution, String aggregate, List<RequestParameterDto> parameterDtos) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.resolution = resolution;
        this.aggregate = aggregate;
        this.parameterDtos = parameterDtos;
    }
}

@Data
class RequestParameterDto {
    private Long organizationId;
    private Long parameterId;

    public RequestParameterDto(Long organizationId, Long parameterId) {
        this.organizationId = organizationId;
        this.parameterId = parameterId;
    }

    @Override
    public String toString() {
        return "RequestParameterDto{" +
                "organizationId=" + organizationId +
                ", parameterId=" + parameterId +
                '}';
    }
}


