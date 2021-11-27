package com.reeco.ingestion.application.mapper;

public interface DomainEntityMapper<D, E> {

    D toDomain(E port);

    E toPersistence(D domain);
}
