package com.reeco.core.dmp.core.until;

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
}
