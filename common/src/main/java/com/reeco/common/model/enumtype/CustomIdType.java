package com.reeco.common.model.enumtype;

import java.util.Optional;

public enum CustomIdType {
    WORKSPACE, CONNECTION, STATION, PARAMETER, ORGANIZATION;

    public static Optional<CustomIdType> from(String text) {
        return Optional.ofNullable(text)
                .map(String::toUpperCase)
                .map(CustomIdType::valueOf);
    }
}
