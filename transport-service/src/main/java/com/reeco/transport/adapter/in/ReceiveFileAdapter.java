package com.reeco.transport.adapter.in;

import com.reeco.transport.application.port.in.ReceiveFilePort;
import com.reeco.transport.utils.annotators.Adapter;
import com.reeco.transport.domain.DataRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class ReceiveFileAdapter implements ReceiveFilePort {

    @Override
    public DataRecord receiveFile(){
        return new DataRecord(LocalDateTime.MIN,"hello",10.0,1,LocalDateTime.now(),null,null);
    }
}
