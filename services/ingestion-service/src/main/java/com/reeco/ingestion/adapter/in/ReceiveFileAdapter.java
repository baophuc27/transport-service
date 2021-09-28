package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.application.port.in.ReceiveFilePort;
import com.reeco.ingestion.domain.DataRecord;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class ReceiveFileAdapter implements ReceiveFilePort {

    @Override
    public DataRecord receiveFile(){
        return new DataRecord(1,LocalDateTime.MIN,"hello",10.0,1,LocalDateTime.now(),null,null);
    }
}
