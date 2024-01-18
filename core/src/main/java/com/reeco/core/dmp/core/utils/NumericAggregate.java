package com.reeco.core.dmp.core.utils;

import com.reeco.core.dmp.core.dto.AggregateMethod;
import com.reeco.core.dmp.core.dto.Resolution;
import com.reeco.core.dmp.core.dto.DataPointDto;
import com.reeco.core.dmp.core.model.cassandra.Alarm;
import com.reeco.core.dmp.core.model.cassandra.NumericalStatByOrg;
import com.reeco.core.dmp.core.model.cassandra.NumericalTsByOrg;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class NumericAggregate {

    private static Interpolate interpolate = new Interpolate();

    public static List<DataPointDto> calculateNumericData(List<NumericalTsByOrg> numericalTsByOrgs, Resolution resolution, List<Alarm> alarms, AggregateMethod aggregate){
        if (resolution.equals(Resolution.DEFAULT)) {
            return numericalTsByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }

        List<DataPointDto> dataPointDtoList = new ArrayList<>();

        Map<Object, List<NumericalTsByOrg>> dataGroup = numericalTsByOrgs.stream().collect(Collectors.groupingBy(e -> {
            int minutes = e.getPartitionKey().getEventTime().getMinute();
            int hours = e.getPartitionKey().getEventTime().getHour();
            int minutesOver = minutes % (int)(resolution.getValueFromEnum()*60);
            int hoursOver = 0;
            if (resolution != Resolution.MIN_30 && resolution != Resolution.DEFAULT) {
                hoursOver = hours % (int) (resolution.getValueFromEnum() * 1);
            }
            return e.getPartitionKey().getEventTime().truncatedTo(ChronoUnit.MINUTES).withMinute(minutes - minutesOver)
                    .withHour(hours - hoursOver);
        }));

        for (Map.Entry<Object, List<NumericalTsByOrg>> entry: dataGroup.entrySet()){
            DataPointDto dataPointDto = new DataPointDto();
            // Interpolation
            if (aggregate == AggregateMethod.INTERPOLATED) {
                Double point = (double) ((LocalDateTime) entry.getKey()).toEpochSecond(ZoneOffset.UTC);
                List<Double> functionValuesX = numericalTsByOrgs.stream().map(DataPointDto::new)
                        .filter(x -> !Objects.equals(x.getValue(), "null"))
                        .map(DataPointDto::getEventTime)
                        .map(eventTime -> (double) eventTime.toEpochSecond(ZoneOffset.UTC))
                        .collect(Collectors.toList());
                List<Double> functionValuesY = numericalTsByOrgs.stream().map(DataPointDto::new)
                        .map(DataPointDto::getValue)
                        .filter(value -> !Objects.equals(value, "null"))
                        .map(Double::valueOf)
                        .collect(Collectors.toList());

                Double linearPoint;
                Integer pointIndex = functionValuesX.indexOf(point);
                if (pointIndex == -1) {
                    Collections.reverse(functionValuesX);
                    Collections.reverse(functionValuesY);
                    try {
                        linearPoint = interpolate.evaluateLinear(point, functionValuesX, functionValuesY);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    linearPoint = functionValuesY.get(pointIndex);
                }
                dataPointDto.setInterpolated(linearPoint.toString());
            }

            // Other Aggregate method
            DoubleSummaryStatistics stats = entry.getValue().stream()
                    .filter(x -> x.getValue() != null)
                    .mapToDouble(NumericalTsByOrg::getValue)
                    .summaryStatistics();
            Long count = (long) entry.getValue().size();
            Double max = stats.getMax();
            Double min = stats.getMin();
            Double sum = stats.getSum();
            Double mean = sum / count;
            Double median = Comparison.median(entry.getValue().stream().map(NumericalTsByOrg::getValue)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            Double range = max - min;
            Double start = entry.getValue().get(entry.getValue().size()-1).getValue();
            Double end = entry.getValue().get(0).getValue();
            Double delta = end - start;


            dataPointDto.setEventTime((LocalDateTime)entry.getKey());
            dataPointDto.setValue(mean.toString());
//            dataPointDto.setLat(entry.getValue().get(0).getLat());
//            dataPointDto.setLon(entry.getValue().get(0).getLon());
            for (Alarm alarm: alarms){
                if(Comparison.checkMatchingAlarmCondition(alarm,mean)){
                    dataPointDto.setIsAlarm(Boolean.TRUE);
                    dataPointDto.setAlarmType(alarm.getAlarmType().toString());
                    dataPointDto.setAlarmId(alarm.getPartitionKey().getAlarmId());
                    break;
                }
            }
            dataPointDto.setMax(max.toString());
            dataPointDto.setMin(min.toString());
            dataPointDto.setMedian(median.toString());
            dataPointDto.setSum(sum.toString());
            dataPointDto.setMean(mean.toString());
            dataPointDto.setRange(range.toString());
            dataPointDto.setStart(start.toString());
            dataPointDto.setEnd(end.toString());
            dataPointDto.setDelta(delta.toString());
            dataPointDto.setCount(count);
            dataPointDtoList.add(dataPointDto);
        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
        return dataPointDtoList;
    }

    public static List<DataPointDto> calculateNumericDataDate(List<NumericalStatByOrg> numericalStatByOrgs, Resolution resolution, LocalDate sDate, List<Alarm> alarms, AggregateMethod aggregate){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();

        Map<Object, List<NumericalStatByOrg>> groupDate = numericalStatByOrgs.stream().collect(Collectors.groupingBy(event ->
                Math.floor((ChronoUnit.DAYS.between(sDate, event.getPartitionKey().getDate()))/(resolution.getValueFromEnum()/24))));
        for (Map.Entry<Object, List<NumericalStatByOrg>> entry: groupDate.entrySet()){
            DataPointDto dataPointDto = new DataPointDto();
            LocalDateTime eventTime = sDate.plusDays(Math.round((Double)entry.getKey() * (resolution.getValueFromEnum()/24))).atStartOfDay();

            // Interpolation
            if (aggregate == AggregateMethod.INTERPOLATED) {
                Double point = (double) eventTime.toEpochSecond(ZoneOffset.UTC);
                List<Double> functionValuesX = numericalStatByOrgs.stream().map(DataPointDto::new)
                        .filter(x -> !Objects.equals(x.getValue(), "null"))
                        .map(DataPointDto::getEventTime)
                        .map(time -> (double) time.toEpochSecond(ZoneOffset.UTC))
                        .collect(Collectors.toList());
                List<Double> functionValuesY = numericalStatByOrgs.stream().map(DataPointDto::new)
                        .map(DataPointDto::getMean)
                        .filter(value -> !Objects.equals(value, "null"))
                        .map(Double::valueOf)
                        .collect(Collectors.toList());

                Double linearPoints;
                Integer pointIndex = functionValuesX.indexOf(point);
                if (pointIndex == -1) {
                    Collections.reverse(functionValuesX);
                    Collections.reverse(functionValuesY);
                    try {
                        linearPoints = interpolate.evaluateLinear(point, functionValuesX, functionValuesY);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    linearPoints = functionValuesY.get(pointIndex);
                }
                dataPointDto.setInterpolated(linearPoints.toString());
            }

            // Other Aggregate method
            Double sum = 0d;
            Double max = 0d;
            Double min = Double.MAX_VALUE;
            Long count = 0L;
            for (NumericalStatByOrg numericalStatByOrg: entry.getValue()){
                if(numericalStatByOrg.getMax() > max) max = numericalStatByOrg.getMax();
                if(numericalStatByOrg.getMin() <= min) min =numericalStatByOrg.getMin();
                sum += numericalStatByOrg.getAcc();
                count += numericalStatByOrg.getCount();
            }
            Double mean = sum / count;
            Double median = Comparison.median(entry.getValue().stream().map(NumericalStatByOrg::getMedian)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            Double range = max - min;
            Double start = entry.getValue().get(entry.getValue().size()-1).getMean();
            Double end = entry.getValue().get(0).getMean();
            Double delta = end - start;


            dataPointDto.setEventTime(eventTime);
            dataPointDto.setValue(mean.toString());
            for (Alarm alarm: alarms){
                if(Comparison.checkMatchingAlarmCondition(alarm,Comparison.roundNum(sum/count,2))){
                    dataPointDto.setIsAlarm(Boolean.TRUE);
                    dataPointDto.setAlarmType(alarm.getAlarmType().toString());
                    dataPointDto.setAlarmId(alarm.getPartitionKey().getAlarmId());
                    break;
                }
            }
            dataPointDto.setMax(max.toString());
            dataPointDto.setMin(min.toString());
            dataPointDto.setMedian(median.toString());
            dataPointDto.setSum(sum.toString());
            dataPointDto.setMean(mean.toString());
            dataPointDto.setRange(range.toString());
            dataPointDto.setStart(start.toString());
            dataPointDto.setEnd(end.toString());
            dataPointDto.setDelta(delta.toString());
            dataPointDto.setCount(count);
            dataPointDtoList.add(dataPointDto);
        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
        return  dataPointDtoList;
    }
}
