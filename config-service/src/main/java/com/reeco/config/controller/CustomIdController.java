package com.reeco.config.controller;

import com.reeco.common.model.dto.CustomId;
import com.reeco.config.controller.base.BaseController;
import com.reeco.config.service.CustomIdService;
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

@RestController
@RequestMapping(value = "/config/customid", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Custom ID Management", description = "Endpoints for managing custom identifiers")
public class CustomIdController extends BaseController {
    private final CustomIdService customIdService;

    @PostMapping
    @Operation(summary = "Create or update custom ID",
            description = "Creates a new custom ID or updates an existing one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Custom ID created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid custom ID data or type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> createCustomId(
            @RequestBody CustomId customIdPayload) {

        return customIdService.createCustomId(customIdPayload);
    }

    @DeleteMapping("/{customIdType}/{id}")
    @Operation(summary = "Delete custom ID",
            description = "Deletes a custom ID by its type and original ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Custom ID deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid custom ID type or ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> deleteCustomId(
            @Parameter(description = "Custom ID type (CONNECTION, WORKSPACE, PARAMETER, ORGANIZATION, STATION)", required = true)
            @PathVariable("customIdType") String idType,
            @Parameter(description = "Original ID", required = true)
            @PathVariable("id") Integer originalId) {

        return customIdService.deleteCustomId(idType, originalId);
    }
}