package com.reeco.common.model.enumtype;

public enum FrequenceType {
    S(0.166667), M(1d), H(60d), D(1440d);

    private Double value;

    FrequenceType(Double value) {
        this.value = value;
    }

    public Double getValueFromEnum() {
        return value;
    }
}