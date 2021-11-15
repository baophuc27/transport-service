package com.reeco.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.exception.ReecoException;
import com.reeco.model.dto.ParameterDTO;
import com.reeco.model.dto.FTPConnectionDTO;

import java.util.Map;

public class EntityModelFactory {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static Connection getConnectionDTOByProtocol(String protocol, Map<String, Object> connectionPayload) throws IllegalArgumentException, ReecoException{
        return getConnectionDTOByProtocol(Protocol.valueOf(protocol), connectionPayload);
    }

    public static Parameter getParameterDTOByAttributeType(String parameter, Map<String, Object> parameterPayload) throws IllegalArgumentException, ReecoException{
        return getParameterDTOByAttributeType(ParamType.valueOf(parameter), parameterPayload);
    }

    public static Connection getConnectionDTOByProtocol(Protocol protocol, Map<String, Object> connectionPayload) throws IllegalArgumentException, ReecoException{
        switch (protocol){
            case FTP:
            case FTPS:
                return objectMapper.convertValue(connectionPayload, FTPConnectionDTO.class);
        }
        throw new ReecoException("Connection Type " + protocol + " is not supported!");
    }

    public static Parameter getParameterDTOByAttributeType(ParamType parameter, Map<String, Object> parameterPayload) throws IllegalArgumentException, ReecoException{
        switch (parameter){
            case COMPUTED:
                return objectMapper.convertValue(parameterPayload, ParameterDTO.class);
        }
        throw new ReecoException("Parameter Type " + parameter + " is not supported!");
    }
}
