package com.reeco.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.exception.ReecoException;
import com.reeco.common.model.dto.BaseConnection;
import com.reeco.common.model.dto.FTPConnection;
import com.reeco.common.model.enumtype.Protocol;

import java.util.Map;

public class EntityModelFactory {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static BaseConnection getConnectionDTOByProtocol(String protocol, Map<String, Object> connectionPayload) throws IllegalArgumentException, ReecoException {
        return getConnectionDTOByProtocol(Protocol.valueOf(protocol), connectionPayload);
    }

    public static BaseConnection getConnectionDTOByProtocol(Protocol protocol, Map<String, Object> connectionPayload) throws IllegalArgumentException, ReecoException{
        switch (protocol){
            case FTP:
            case FTPS:
                return objectMapper.convertValue(connectionPayload, FTPConnection.class);
        }
        throw new ReecoException("Connection Type " + protocol + " is not supported!");
    }

}
