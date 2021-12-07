package com.reeco.core.dmp.core.until;

import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.core.dmp.core.model.Alarm;

import java.util.List;

public class Comparison {
    public static Double roundNum(Double value, int places) {
        if(value==null){
            return null;
        }

        Double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static double median (List<Double> a){
        int middle = a.size()/2;

        if (a.size() % 2 == 1) {
            return a.get(middle);
        } else {
            return (a.get(middle-1) + a.get(middle)) / 2.0;
        }
    }
    public static double std (List<Double> a, Double mean){
        Double sum = 0d;
        for (Double i : a)
            sum += Math.pow((i - mean), 2);
        return Math.sqrt( sum / ( a.size() - 1 ) ); // sample
    }

    public static boolean isInBracketRange(Alarm alarm, Double value) {
        return value > Double.parseDouble(alarm.getMinValue())
                && value < Double.parseDouble(alarm.getMaxValue());
    }
    public static boolean isInSquareRange(Alarm alarm, Double value) {
        return value >= Double.parseDouble(alarm.getMinValue())
                && value <= Double.parseDouble(alarm.getMaxValue());
    }

    public static boolean isExactEqualNumber(Alarm alarm, Double value) {
        return value.equals(Double.valueOf(alarm.getMinValue()));
    }

    public static boolean isExactEqualCategorical(Alarm alarm, String value) {
        return value.equals(alarm.getMinValue());
    }

    public static boolean checkMatchingAlarmCondition(Alarm alarm, Double value) {
        if (alarm.getAlarmType() == AlarmType.THRESHOLD) {
            return isExactEqualNumber(alarm, value);
        } else if (alarm.getAlarmType() == AlarmType.SQUARE_RANGE) {
            return isInSquareRange(alarm, value);
        } else if (alarm.getAlarmType() == AlarmType.BRACKET_RANGE) {
            return isInBracketRange(alarm, value);
        }
        return false;
    }
}
