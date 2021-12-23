package com.reeco.http.cache;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.ParameterCache;
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
            connection.setParameterList(paramsByOrgs.stream().map(ParameterCache::new).collect(Collectors.toList()));
            put(connection);
        }
    }

    public void put(Connection connection){
        if(connection!=null){
            getCache().put(connection.getConnectionId().toString(),connection);
        }
    }

    public void put(ParameterCache parameterCache){
        if (parameterCache!=null){
            get(parameterCache.getConnectionId().toString()).getParameterList().add(parameterCache);
        }
    }

    public Connection get(String key){
        return getCache().get(key, Connection.class);
    }



    public void evict(String key){
         getCache().evict(key);
    }

    public void evict(ParameterCache parameterCache){
        if (get(parameterCache.getConnectionId().toString()) == null){
            return;
        }
        ParameterCache parameterCache1 = get(parameterCache.getConnectionId().toString())
                .getParameterList().stream().filter(pr -> pr.getParamId().equals(parameterCache.getParamId()))
                        .findFirst().orElse(null);
        get(parameterCache.getConnectionId().toString()).getParameterList().remove(parameterCache1);
    }
}
