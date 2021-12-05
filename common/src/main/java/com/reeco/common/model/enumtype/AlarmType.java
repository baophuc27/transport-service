package com.reeco.common.model.enumtype;

import java.util.Optional;

public enum AlarmType {
    THRESHOLD, SQUARE_RANGE, BRACKET_RANGE;

    public static Optional<AlarmType> from(String text) {
        return Optional.ofNullable(text)
                .map(String::toUpperCase)
                .map(AlarmType::valueOf);
    }
}