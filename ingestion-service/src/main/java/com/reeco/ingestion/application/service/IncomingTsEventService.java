package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.mapper.RuleEngineEventMapper;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.CategoricalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class IncomingTsEventService implements StoreTsEventUseCase {

    @Autowired
    private NumericalTsByOrgRepository numericalTsByOrgRepository;
    @Autowired
    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    private RuleEngineEventMapper ruleEngineEventMapper;

    @Override
    public void storeEvent(RuleEngineEvent event, Indicator indicator) {
        switch (indicator.getValueType()){
            case NUMBER: {
                NumericalTsByOrg numTsEvent = ruleEngineEventMapper.toNumPersistence(event);
                numTsEvent.setValue(Double.valueOf(event.getValue()));
                numericalTsByOrgRepository
                        .save(numTsEvent)
                        .subscribe(v->log.info("Inserted Numeric Event: {}", v));
                break;
            }
            case STRING: {
                CategoricalTsByOrg catTsEvent = ruleEngineEventMapper.toCatPersistence(event);
                categoricalTsByOrgRepository
                        .save(catTsEvent)
                        .subscribe(v->log.info("Inserted Cat Event: {}", v));
                break;
            }
            default: break;
        }

    }

}
