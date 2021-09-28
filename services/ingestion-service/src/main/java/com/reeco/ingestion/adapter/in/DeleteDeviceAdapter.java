package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import com.reeco.ingestion.infrastructure.persistence.postgresql.PostgresFTPRepository;
import com.reeco.ingestion.application.mapper.ConnectionMapper;
import com.reeco.ingestion.application.port.in.DeleteDevicePort;
import com.reeco.ingestion.application.usecase.DeleteDeviceCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

@Slf4j
public class DeleteDeviceAdapter implements DeleteDevicePort {

    @Autowired
    private ConnectionMapper connectionMapper;

    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Autowired
    private PostgresFTPRepository postgresFTPRepository;

    @Override
    public void deleteDevice(DeleteDeviceCommand command){
        try {
            postgresDeviceRepository.deleteById(command.getDeviceId());
        }
        catch (EmptyResultDataAccessException ex){
            log.warn("No device to be deleted id: {}",command.getDeviceId());
        }
    }
}
