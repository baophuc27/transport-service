package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.domain.ParamsAndOrg;
import reactor.core.publisher.Flux;

public interface LoadOrgAndParamPort {
    Flux<ParamsAndOrg> findAllParamsGroupByOrg();
}
