package com.reeco.common.model.enumtype;

public enum FrequenceType {
    SECOND(0.166667), MINUTE(1d), HOUR(60d), DAY(1440d);

    private Double value;

    FrequenceType(Double value) {
        this.value = value;
    }

    public Double getValueFromEnum() {
        return value;
    }
}