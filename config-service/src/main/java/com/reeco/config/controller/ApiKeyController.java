package com.reeco.config.controller;

import com.reeco.config.controller.base.BaseController;
import com.reeco.config.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@RestController
@RequestMapping(value = "/config/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Key Management", description = "Endpoints for managing API keys and MQTT connections")
public class ApiKeyController extends BaseController {
    private final ApiKeyService apiKeyService;

    @PostMapping("/api-key")
    @Operation(summary = "Create or update API key",
            description = "Creates a new API key or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API key created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid API key data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> upsertAPIKey(
            @RequestBody Map<String, Object> apiKeyPayload) {

        return apiKeyService.upsertAPIKey(apiKeyPayload);
    }

    @DeleteMapping("/api-key/{id}")
    @Operation(summary = "Delete API key",
            description = "Deletes an API key by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API key deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid API key ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> deleteApiKey(
            @Parameter(description = "API key ID", required = true)
            @PathVariable("id") String id) {

        return apiKeyService.deleteApiKey(id);
    }

    @PostMapping("/mqtt-sender")
    @Operation(summary = "Create or update MQTT share",
            description = "Creates a new MQTT share or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MQTT share created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid MQTT share data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> upsertMQTTShare(
            @RequestBody Map<String, Object> mqttPayload) {

        return apiKeyService.upsertMQTTShare(mqttPayload);
    }

    @DeleteMapping("/mqtt-sender/{id}")
    @Operation(summary = "Delete MQTT share",
            description = "Deletes a MQTT share by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MQTT share deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid MQTT share ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> deleteMqttShare(
            @Parameter(description = "MQTT share ID", required = true)
            @PathVariable("id") String id) {

        return apiKeyService.deleteMqttShare(id);
    }
}