package com.reeco.http.cache;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.Parameter;
import com.reeco.http.model.entity.ConnectionByOrg;
import com.reeco.http.model.entity.ParamsByOrg;
import com.reeco.http.model.repo.ConnectionByOrgRepository;
import com.reeco.http.model.repo.ParamsByOrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConnectionCache {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    ConnectionByOrgRepository connectionByOrgRepository;

    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    private final String CONNECTION_CACHE = "connection_cache";

    public Cache getCache(){
        return cacheManager.getCache(CONNECTION_CACHE);
    }

    public void loadDataToCache(){
        List<ConnectionByOrg> connectionByOrg = connectionByOrgRepository.getByTransportType(TransportType.HTTP);
        for (ConnectionByOrg connectionEntity: connectionByOrg){
            Connection connection = new Connection(connectionEntity);
            List<ParamsByOrg> paramsByOrgs = paramsByOrgRepository.findParamByConnection(connection.getOrgId(),connection.getConnectionId());
            connection.setParameterList(paramsByOrgs.stream().map(Parameter::new).collect(Collectors.toList()));
            put(connection);
        }
    }

    public void put(Connection connection){
        if(connection!=null){
            getCache().put(connection.getConnectionId().toString(),connection);
        }
    }

    public void put(Parameter parameter){
        if (parameter!=null){
            get(parameter.getConnectionId().toString()).getParameterList().add(parameter);
        }
    }

    public Connection get(String key){
        return getCache().get(key, Connection.class);
    }



    public void evict(String key){
         getCache().evict(key);
    }

    public void evict(Parameter parameter){
        get(parameter.getConnectionId().toString()).getParameterList().remove(parameter);
    }
}
