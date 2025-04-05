package com.reeco.config.controller;

import com.reeco.config.controller.base.BaseController;
import com.reeco.config.service.ConnectionService;
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
@RequestMapping(value = "/config/connection", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Connection Management", description = "Endpoints for managing connections (FTP, FTPS, HTTP)")
public class ConnectionController extends BaseController {
    private final ConnectionService connectionService;

    @PostMapping("/{protocol}")
    @Operation(summary = "Create or update connection",
            description = "Creates a new connection or updates an existing one based on protocol (ftp, ftps, http)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection created/updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid connection data or unsupported protocol"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> createConnection(
            @Parameter(description = "Protocol type (ftp, ftps, http)", required = true)
            @PathVariable("protocol") String protocol,
            @RequestBody Map<String, Object> connectionPayload) {

        return connectionService.createConnection(protocol, connectionPayload);
    }

    @DeleteMapping("/{protocol}/{orgId}/{id}")
    @Operation(summary = "Delete connection",
            description = "Deletes a connection by its ID, organization ID, and protocol type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid connection ID or unsupported protocol"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public DeferredResult<ResponseEntity<String>> deleteConnection(
            @Parameter(description = "Connection ID", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Protocol type (ftp, ftps, http)", required = true)
            @PathVariable("protocol") String protocol,
            @Parameter(description = "Organization ID", required = true)
            @PathVariable("orgId") Long orgId) {

        if ("http".equalsIgnoreCase(protocol)) {
            return connectionService.deleteHttpConnection(id, orgId);
        } else if ("ftp".equalsIgnoreCase(protocol) || "ftps".equalsIgnoreCase(protocol)) {
            return connectionService.deleteFtpConnection(id, orgId);
        } else {
            // This will be handled by the exception handler
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }
}