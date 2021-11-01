package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.mapper.TsEventMapper;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.CategoricalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Adapter
@Log4j2
public class TsEventPersistenceAdapter implements TsEventRepository {

    @Autowired
    private IndicatorInfoRepository indicatorRepository;

    @Autowired
    private NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    private TsEventMapper tsEventMapper;


    @Override
    public void insertNumericEvent(NumericalTsEvent event) {
        numericalTsByOrgRepository
                .save(tsEventMapper.toPort(event))
                .subscribe(v -> log.info(v.toString()));
    }

    @Override
    public void insertCategoricalEvent(CategoricalTsEvent event) {
        categoricalTsByOrgRepository
                .save(tsEventMapper.toPort(event))
                .subscribe(v -> log.info(v.toString()));
    }

}
