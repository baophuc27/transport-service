package com.reeco.transport.adapter.out;

import com.reeco.transport.application.mapper.ConnectionMapper;
import com.reeco.transport.application.port.out.StoreConfigurationPort;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.FtpEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresFTPRepository;
import com.reeco.transport.utils.exception.EventProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PersistenceAdapter implements StoreConfigurationPort {

    @Autowired
    private ConnectionMapper connectionMapper;

    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Autowired
    private PostgresFTPRepository postgresFTPRepository;

    @Override
    public void save(DeviceConnection deviceConnection) throws EventProcessingException {
        DeviceEntity deviceEntity = connectionMapper.domainToDeviceEntity(deviceConnection);
        deviceEntity.setProtocolType("FTP");
        try{
            postgresDeviceRepository.save(deviceEntity);
        }
        catch (RuntimeException exception){
            log.warn("Error when register new device");
            throw new EventProcessingException(exception.getMessage());
        }


        FtpEntity ftpEntity = connectionMapper.domainToFtpEntity(deviceConnection);
        log.warn(String.valueOf(ftpEntity));
        ftpEntity.setHostName("103.88.122.104");
        ftpEntity.setHostPort(2100);
        Integer ftpId = postgresFTPRepository.findFtpByUserName(ftpEntity.getUserName());
        if (ftpId != null && ftpId != ftpEntity.getId()){
            throw new EventProcessingException("Duplicate ftp username");
        }
        try{
            postgresFTPRepository.save(ftpEntity);
        }
        catch (RuntimeException exception){
            log.warn("Error when register new device");
            throw new EventProcessingException(exception.getMessage());
        }
    }
}
