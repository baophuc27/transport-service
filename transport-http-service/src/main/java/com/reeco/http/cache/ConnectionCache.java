package com.reeco.http.cache;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.model.Connection;
import com.reeco.http.model.ParameterCache;
import com.reeco.http.infrastructure.persistence.cassandra.entity.ConnectionByOrg;
import com.reeco.http.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.http.infrastructure.persistence.cassandra.repository.ConnectionByOrgRepository;
import com.reeco.http.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
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
            connection.setParameterList(paramsByOrgs.stream().map(ParameterCache::new).collect(Collectors.toList()));
            put(connection);
        }
    }

    public void put(Connection connection){

        if(connection!=null){
            String key = connection.getConnectionId().toString() + "%" +connection.getAccessToken();
            log.info("Save key: {} with connection: {}", key,connection.toString());
            getCache().put(key,connection);
        }
        else{
            log.warn("Connection with access token: {} is existed.",connection.getAccessToken());
        }
    }

    public void put(ParameterCache parameterCache, String key){
        if (parameterCache!=null){
            get(key).getParameterList().add(parameterCache);
        }
    }

    public Connection get(String key){
        return getCache().get(key, Connection.class);
    }



    public void evict(String key){
         getCache().evict(key);
    }

    public void evict(ParameterCache parameterCache, String key){
        if (get(key) == null){
            return;
        }
        ParameterCache parameterCache1 = get(key)
                .getParameterList().stream().filter(pr -> pr.getParamId().equals(parameterCache.getParamId()))
                        .findFirst().orElse(null);
        get(key).getParameterList().remove(parameterCache1);
    }
}
