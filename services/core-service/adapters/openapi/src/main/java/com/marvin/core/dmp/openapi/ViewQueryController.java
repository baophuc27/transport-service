package com.marvin.core.dmp.openapi;

import com.marvin.core.dmp.application.domain.governance.View;
import com.marvin.core.dmp.application.port.in.ViewQuery;
import com.marvin.core.dmp.common.OpenApiAdapter;
import com.marvin.shares.api.dmp.view.ViewDto;
import com.marvin.shares.api.dmp.view.ViewService;
import com.marvin.shares.util.exceptions.InvalidInputException;
import com.marvin.shares.util.exceptions.NotFoundException;
import com.marvin.shares.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;


@OpenApiAdapter
@RestController
@RequiredArgsConstructor
public class ViewQueryController implements ViewService {

    private static final Logger LOG = LoggerFactory.getLogger(ViewQueryController.class);

    private final ServiceUtil serviceUtil;

    private  final ViewQuery viewQuery;

    private  final ViewMapper viewMapper;

    @Override
    public Mono<ViewDto> getView(int viewId) {
        LOG.debug("/view return the found view for viewId={}", viewId);

        if (viewId < 1) throw new InvalidInputException("Invalid viewId: " + viewId);

        return viewQuery.getView(viewId)
                .switchIfEmpty(error(new NotFoundException("No view found for viewId: " + viewId)))
                .log()
                .map(e -> viewMapper.entityToApi(e))
                .map(e -> {e.setServiceAddress(serviceUtil.getServiceAddress()); return e;});

    }

    @Override
    public Mono<ViewDto> createView(ViewDto body) {

        if (body.getViewId() < 1) throw new InvalidInputException("Invalid accountId: " + body.getViewId());

        try {

            LOG.debug("createView: creates a new account entity for userId: {}", body.getUserId());
            View viewEntity = viewMapper.apiToEntity(body);
            Mono<ViewDto> newEntity = viewQuery.saveView(viewEntity)
                    .log()
                    .onErrorMap(
                            DuplicateKeyException.class,
                            ex -> new InvalidInputException("Duplicate key, View Id: " + body.getViewId()))
                    .map(e -> viewMapper.entityToApi(e));

            return newEntity;


        } catch (RuntimeException re) {
            LOG.warn("createView failed: {}", re.toString());
            throw re;
        }
    }

    @Override
    public Mono<Void> deleteView(int viewId) {
        if (viewId < 1) throw new InvalidInputException("Invalid viewId: " + viewId);

        LOG.debug("deleteView: tries to delete an entity with viewId: {}", viewId);
        return viewQuery.deleteView(viewId);
    }


}