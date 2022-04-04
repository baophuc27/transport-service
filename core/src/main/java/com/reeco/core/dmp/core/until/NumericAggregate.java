package com.reeco.core.dmp.core.until;

import com.reeco.core.dmp.core.dto.Resolution;
import com.reeco.core.dmp.core.dto.DataPointDto;
import com.reeco.core.dmp.core.model.Alarm;
import com.reeco.core.dmp.core.model.NumericalStatByOrg;
import com.reeco.core.dmp.core.model.NumericalTsByOrg;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class NumericAggregate {
    public static List<DataPointDto> calculateNumericData(List<NumericalTsByOrg> numericalTsByOrgs, Resolution resolution, List<Alarm> alarms){
//        Collections.reverse(numericalTsByOrgs);
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if (resolution.equals(Resolution.DEFAULT)){
            return numericalTsByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
        Map<Object, List<NumericalTsByOrg>> dataGroup = numericalTsByOrgs.stream().collect(Collectors.groupingBy(e -> {
            int minutes = e.getPartitionKey().getEventTime().getMinute();
            int minutesOver = minutes % (int)(resolution.getValueFromEnum()*60);
            return e.getPartitionKey().getEventTime().truncatedTo(ChronoUnit.MINUTES).withMinute(minutes - minutesOver);
        }));
        for (Map.Entry<Object, List<NumericalTsByOrg>> entry: dataGroup.entrySet()){
            DoubleSummaryStatistics stats = entry.getValue().stream()
                    .mapToDouble(NumericalTsByOrg::getValue)
                    .summaryStatistics();
            Double mean = Comparison.roundNum(stats.getAverage(),2);
            Double max = stats.getMax();
            Double min = stats.getMin();
            Double median = Comparison.median(entry.getValue().stream().map(NumericalTsByOrg::getValue).collect(Collectors.toList()));
            DataPointDto dataPointDto = new DataPointDto();
            dataPointDto.setEventTime((LocalDateTime)entry.getKey());
            dataPointDto.setValue(mean.toString());
            dataPointDto.setLat(entry.getValue().get(0).getLat());
            dataPointDto.setLon(entry.getValue().get(0).getLon());
            for (Alarm alarm: alarms){
                if(Comparison.checkMatchingAlarmCondition(alarm,mean)){
                    dataPointDto.setIsAlarm(Boolean.TRUE);
                    dataPointDto.setAlarmType(alarm.getAlarmType().toString());
                    dataPointDto.setAlarmId(alarm.getPartitionKey().getAlarmId());
                    break;
                }
            }
            dataPointDto.setCount((long) entry.getValue().size());
            dataPointDto.setMax(max.toString());
            dataPointDto.setMin(min.toString());
            dataPointDto.setMedian(median.toString());
            dataPointDto.setMean(mean.toString());
            dataPointDtoList.add(dataPointDto);
        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
//        Timestamp sTime =  Timestamp.valueOf(numericalTsByOrgs.get(0).getPartitionKey().getEventTime());
//        Long rangeTime = 0l;
//        Double sumValue =0d;
//        Long count = 0l;
//        dataPointDtoList.add(new DataPointDto(numericalTsByOrgs.get(0)));
//        for (NumericalTsByOrg numericalTsByOrg: numericalTsByOrgs.subList(1,numericalTsByOrgs.size())){
//            rangeTime = Timestamp.valueOf(numericalTsByOrg.getPartitionKey().getEventTime()).getTime() - sTime.getTime();
//            sumValue += numericalTsByOrg.getValue();
//            count +=1;
//
//            if (chartResolution.equals(ChartResolution.DEFAULT)){
//                return numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
//            }else {
//                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
//                    DataPointDto dataPointDto = new DataPointDto();
//                    dataPointDto.setEventTime(numericalTsByOrg.getPartitionKey().getEventTime());
//                    Double value = sumValue/count;
//                    dataPointDto.setValue(value.toString());
//                    dataPointDtoList.add(dataPointDto);
//                    sTime = Timestamp.valueOf(numericalTsByOrg.getPartitionKey().getEventTime());
//                    sumValue = 0D;
//                    count = 0L;
//                }
//            }
//
//        }
//
//        DataPointDto dataPointDto = new DataPointDto();
//        if (count>0) {
//            dataPointDto.setEventTime(numericalTsByOrgs.get(numericalTsByOrgs.size() - 1).getPartitionKey().getEventTime());
//            Double value = sumValue / count;
//            dataPointDto.setValue(value.toString());
//            dataPointDtoList.add(dataPointDto);
//        }
//        dataPointDtoList.add(new DataPointDto(numericalTsByOrgs.get(numericalTsByOrgs.size()-1)));
        return dataPointDtoList;
    }

    public static List<DataPointDto> calculateNumericDataDate(List<NumericalStatByOrg> numericalStatByOrgs, Resolution resolution, LocalDate sDate, List<Alarm> alarms){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(resolution.equals(Resolution.DAY_1)){
            return numericalStatByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
//        LocalDate sDate = numericalStatByOrgs.get(0).getPartitionKey().getDate();
        Map<Object, List<NumericalStatByOrg>> groupDate = numericalStatByOrgs.stream().collect(Collectors.groupingBy(event ->
                Math.floor((ChronoUnit.DAYS.between(sDate, event.getPartitionKey().getDate()))/(resolution.getValueFromEnum()/24))));
        for (Map.Entry<Object, List<NumericalStatByOrg>> entry: groupDate.entrySet()){
            DoubleSummaryStatistics stats = entry.getValue().stream()
                    .mapToDouble(NumericalStatByOrg::getMean)
                    .summaryStatistics();
            Double mean = Comparison.roundNum(stats.getAverage(),2);
            Double sum = 0d;
            Double max = 0d;
            Double min = Double.MAX_VALUE;
            Long count = 0L;
            for (NumericalStatByOrg numericalStatByOrg: entry.getValue()){
                if(numericalStatByOrg.getMax() > max) max = numericalStatByOrg.getMax();
                if(numericalStatByOrg.getMin() <= min) min =numericalStatByOrg.getMin();
                sum += numericalStatByOrg.getMean();
                count += 1;
            }
            Double median = Comparison.median(entry.getValue().stream().map(NumericalStatByOrg::getMedian).collect(Collectors.toList()));
            DataPointDto dataPointDto = new DataPointDto();
            dataPointDto.setEventTime(sDate.plusDays(Math.round((Double)entry.getKey() * (resolution.getValueFromEnum()/24))).atStartOfDay());
            dataPointDto.setValue(Comparison.roundNum(sum/count,2).toString());
            for (Alarm alarm: alarms){
                if(Comparison.checkMatchingAlarmCondition(alarm,Comparison.roundNum(sum/count,2))){
                    dataPointDto.setIsAlarm(Boolean.TRUE);
                    dataPointDto.setAlarmType(alarm.getAlarmType().toString());
                    dataPointDto.setAlarmId(alarm.getPartitionKey().getAlarmId());
                    break;
                }
            }
            dataPointDto.setCount(count);
            dataPointDto.setMax(max.toString());
            dataPointDto.setMin(min.toString());
            dataPointDto.setMedian(median.toString());
            dataPointDto.setMean(mean.toString());
            dataPointDtoList.add(dataPointDto);
        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
//        else{
//            Timestamp sTime =  Timestamp.valueOf(numericalStatByOrgs.get(0).getPartitionKey().getDate().atStartOfDay());
//            Long rangeTime = 0l;
//            Double sumValue =0d;
//            Long count = 0l;
//            dataPointDtoList.add(new DataPointDto(numericalStatByOrgs.get(0)));
//            for (NumericalStatByOrg numericalStatByOrg: numericalStatByOrgs){
//                rangeTime = Timestamp.valueOf(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay()).getTime() - sTime.getTime();
//                sumValue += numericalStatByOrg.getMean();
//                count +=1;
//                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
//                    DataPointDto dataPointDto = new DataPointDto();
//                    dataPointDto.setEventTime(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay());
//                    Double value = sumValue/count;
//                    dataPointDto.setValue(value.toString());
//                    dataPointDtoList.add(dataPointDto);
//                    sTime = Timestamp.valueOf(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay());
//                    sumValue = 0D;
//                    count = 0L;
//                }
//            }
//            DataPointDto dataPointDto = new DataPointDto();
//            if (count>0) {
//                dataPointDto.setEventTime(numericalStatByOrgs.get(numericalStatByOrgs.size() - 1).getPartitionKey().getDate().atStartOfDay());
//                Double value = sumValue / count;
//                dataPointDto.setValue(value.toString());
//                dataPointDtoList.add(dataPointDto);
//            }
//
//        }
        return  dataPointDtoList;
    }
}
