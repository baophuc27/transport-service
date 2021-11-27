package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.domain.Parameter;
import reactor.core.publisher.Flux;

public interface LoadOrgAndParamPort {
    Flux<Parameter.ParamsByOrg> findAllParamsGroupByOrg();
}
