package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.domain.ParamsByOrg;
import reactor.core.publisher.Flux;

public interface LoadOrgAndParamPort {
    Flux<ParamsByOrg> findAllParamsGroupByOrg();
}
