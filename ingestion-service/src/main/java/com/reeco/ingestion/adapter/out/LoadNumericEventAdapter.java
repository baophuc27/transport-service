//package com.reeco.ingestion.adapter.out;
//
//import com.reeco.ingestion.application.mapper.TsEventMapper;
//import com.reeco.ingestion.application.port.out.AggregateEventsPort;
//import com.reeco.ingestion.domain.NumericalTsEvent;
//import com.reeco.ingestion.domain.CategoricalTsEvent;
//import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.CategoricalTsByOrgRepository;
//import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
//import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalStatByOrgRepository;
//import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
//import com.reeco.ingestion.utils.annotators.Adapter;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import reactor.core.publisher.Mono;
//
//@Adapter
//@Log4j2
//public class LoadNumericEventAdapter implements AggregateEventsPort {
//
//    @Autowired
//    private IndicatorInfoRepository indicatorRepository;
//
//    @Autowired
//    private NumericalTsByOrgRepository numericalTsByOrgRepository;
//
//    @Autowired
//    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;
//
//    @Autowired
//    private NumericalStatByOrgRepository numericalStatByOrgRepository;
//
//
//    @Override
//    public Mono<NumericalTsEvent> aggEvents(Long orgId) {
//        return null;
//    }
//}
