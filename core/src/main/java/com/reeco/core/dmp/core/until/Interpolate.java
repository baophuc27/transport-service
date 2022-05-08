package com.reeco.core.dmp.core.until;

import java.util.ArrayList;
import java.util.List;

public class Interpolate {
    public Interpolate() {
    }

    public Double evaluateLinear (Double pointsToEvaluate, List<Double> functionValuesX, List<Double> functionValuesY) throws Exception {
        if (functionValuesX.size() != functionValuesY.size()) {
            throw new Exception("The lengths of array X and Y must be identical");
        }
        List<Double> results = new ArrayList<>();
        int index = this.findIntervalBorderIndex(pointsToEvaluate, functionValuesX, true);
        if (index == functionValuesX.size() - 1) {
            index--;
        }

        return this.linearInterpolation(
                pointsToEvaluate,
                functionValuesX.get(index),
                functionValuesY.get(index),
                functionValuesX.get(index + 1),
                functionValuesY.get(index + 1)
        );
    }

    public List<Double> evaluateStep (List<Double> pointsToEvaluate, List<Double> functionValuesX, List<Double> functionValuesY, boolean useRightBorder) throws Exception {
        if (functionValuesX.size() != functionValuesY.size()) {
            throw new Exception("The lengths of array X and Y must be identical");
        }
        List<Double> results = new ArrayList<>();
        for (Double point : pointsToEvaluate) {
            results.add(functionValuesY.get(this.findIntervalBorderIndex(point, functionValuesX, useRightBorder)));
        }
        return results;
    }

    public List<Double> evaluateValue (List<Double> pointsToEvaluate, List<Double> functionValuesX, List<Double> functionValuesY, Double fillValue) {
        List<Double> results = new ArrayList<>();
        for (Double point : pointsToEvaluate) {
            int intervalBorderLeft = this.findIntervalBorderIndex(point, functionValuesX, true);
            results.add(functionValuesX.get(intervalBorderLeft) == point ? functionValuesY.get(intervalBorderLeft) : fillValue);
        }
        return results;
    }

    private int findIntervalBorderIndex (Double point, List<Double> intervals, Boolean useRightBorder) {
        if (point < intervals.get(0)) {
            return 0;
        }
        if (point > intervals.get(intervals.size() - 1)) {
            return intervals.size() - 1;
        }

        // If point is inside interval
        // Start searching on a full range of intervals
        int indexOfNumberToCompare;
        int leftBorderIndex = 0;
        int rightBorderIndex = intervals.size() - 1;

        while (rightBorderIndex - leftBorderIndex != 1) {
            indexOfNumberToCompare = leftBorderIndex + (int) Math.floor((rightBorderIndex - leftBorderIndex) / 2);
            if (point >= intervals.get(indexOfNumberToCompare)) {
                leftBorderIndex = indexOfNumberToCompare;
            } else {
                rightBorderIndex = indexOfNumberToCompare;
            }
        }
        return useRightBorder ? rightBorderIndex : leftBorderIndex;
    }

    private Double linearInterpolation (Double x, Double x0, Double y0, Double x1, Double y1) {
        Double a = (y1 - y0) / (x1 - x0);
        Double b = - a * x0 + y0;
        return a * x + b;
    }
}
