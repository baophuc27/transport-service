package com.marvin.shares.api.dmp.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(description = "REST API for views information.")
public interface ViewService {

    /**
     * Sample usage: curl $HOST:$PORT/view/1
     *
     * @param viewId
     * @return the view, if found, else null
     */
    @ApiOperation(
            value = "${api.view.get-view.description}",
            notes = "${api.view.get-view.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @GetMapping(
            value    = "/view/{viewId}",
            produces = "application/json")
    Mono<ViewDto> getView(@PathVariable int viewId);

    /**
     * Sample usage:
     *
     * curl -X POST $HOST:$PORT/view \
     *   -H "Content-Type: application/json" --data \
     *   '{"viewId":123,"name":"investing","userId":42}'
     *
     * @param body
     */
    @ApiOperation(
            value = "${api.view.create-view.description}",
            notes = "${api.view.create-view.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @PostMapping(
            value    = "/view",
            consumes = "application/json")
    Mono<ViewDto> createView(@RequestBody ViewDto body);

    /**
     * Sample usage:
     *
     * curl -X DELETE $HOST:$PORT/view/1
     *
     * @param viewId
     */
    @ApiOperation(
            value = "${api.view.delete-view.description}",
            notes = "${api.view.delete-view.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @DeleteMapping(value = "/view/{viewId}")
    Mono<Void> deleteView(@PathVariable int viewId);
}

