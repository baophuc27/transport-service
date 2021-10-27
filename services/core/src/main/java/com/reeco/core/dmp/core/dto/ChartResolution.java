package com.reeco.core.dmp.core.dto;

public enum ChartResolution {
    DEFAULT(0.15), MIN_30(0.5), HOUR_1(1d), HOUR_2(2d), HOUR_4(4d), HOUR_8(8d), DAY_1(24d), DAY_3(72d), WEEK_1(168d),
    WEEK_2(336d), MONTH(720d);

    private Double value1;

    ChartResolution(Double value) {
        this.value1 = value;
    }

    public Double getValueFromEnum() {
        return value1;
    }
}