package com.reeco.transport.application.mapper;

import com.reeco.transport.infrastructure.model.UpsertApiKeyMessage;
import com.reeco.transport.infrastructure.model.UpsertMQTTMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.ApiKeyEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.MQTTConfigEntity;
import com.reeco.transport.utils.preprocessing.MosquittoPBKDF2;
import org.mapstruct.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.WARN)

public interface ApiKeyMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "permission", target = "permission"),
            @Mapping(source = "apiKey", target = "key"),
            @Mapping(source = "scope.organizationIds", target = "organizationIds",qualifiedBy = SerializeIds.class),
            @Mapping(source = "scope.parameterIds", target = "parameterIds",qualifiedBy = SerializeIds.class),
            @Mapping(source = "scope.connectionIds", target = "connectionIds",qualifiedBy = SerializeIds.class)
    })
    ApiKeyEntity messageToEntity(UpsertApiKeyMessage message);

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source="name",target="name"),
            @Mapping(source="topicName",target = "topic"),
            @Mapping(source="useSSL",target = "useSSL"),
            @Mapping(source="username",target = "username"),
            @Mapping(source="password",target = "password",qualifiedBy = EncodePassword.class),
            @Mapping(source="scope.organizationIds",target = "organizationIds",qualifiedBy = SerializeIds.class),
            @Mapping(source="scope.parameterIds",target = "parameterIds",qualifiedBy = SerializeIds.class),
            @Mapping(source="scope.connectionIds",target = "connectionIds",qualifiedBy = SerializeIds.class),
    })
    MQTTConfigEntity mqttMessageToEntity(UpsertMQTTMessage message);


@SerializeIds
    default String format(List<Integer> ids){
    if (null == ids){
        return "";
    }
    String result = ids.stream().map(String::valueOf)
            .collect(Collectors.joining(","));
    return result;
}

@EncodePassword
    default String format(String plainPassword){
    MosquittoPBKDF2 encoder = new MosquittoPBKDF2();
    String encodedPassword = encoder.createPassword(plainPassword);
    return encodedPassword;
}

}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface SerializeIds{}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface EncodePassword{}