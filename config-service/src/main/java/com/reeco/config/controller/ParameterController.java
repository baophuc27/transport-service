package com.reeco.config.controller;

import com.reeco.common.model.dto.Parameter;
import com.reeco.config.controller.base.BaseController;
import com.reeco.config.service.ParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(value = "/config/parameter", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Parameter Management", description = "Endpoints for managing configuration parameters")
public class ParameterController extends BaseController {
    private final ParameterService parameterService;

    @PostMapping
    @Operation(summary = "Create or update parameter",
            description = "Creates a new parameter or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> createParams(@RequestBody Parameter parameter) {
        return parameterService.createParameter(parameter);
    }

    @DeleteMapping("/{orgId}/{connectionId}/{id}")
    @Operation(summary = "Delete parameter",
            description = "Deletes a parameter by its ID, connection ID, and organization ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> deleteParams(
            @PathVariable("id") Long id,
            @PathVariable("connectionId") Long connectionId,
            @PathVariable("orgId") Long orgId) {
        return parameterService.deleteParameter(id, connectionId, orgId);
    }
}