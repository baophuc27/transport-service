package com.reeco.core.dmp.core.service;


import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.ValueType;
import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
import com.reeco.core.dmp.core.until.ApiResponse;
import com.reeco.core.dmp.core.until.Comparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private IndicatorInfoRepository indicatorInfoRepository;

    @Autowired
    private ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    private NumericalStatByOrgRepository numericalStatByOrgRepository;

    @Autowired
    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    private  CategoricalStatByOrgRepository categoricalStatByOrgRepository;

    @Autowired
    private AlarmRepository alarmRepository;


    public ChartDto historyData(ChartDto chartDto) throws Exception{
        System.out.println(chartDto);
        // Timestamp startTime = new Timestamp(chartDto.getStartTime().getTime());
        if (chartDto.getStartTime().isAfter(chartDto.getEndTime())) {
            throw new Exception("Invalid Time Range");
        }
        ChartDto chartDto1 = new ChartDto();

        // chartDto1.setStationId(chartDto.getStationId());
        // chartDto1.setParameterDtos(chartDto.getParameterDtos());



        List<ParameterDataDto> indicatorDataDtos = new ArrayList<>();
        for (ParameterDto indicatorDto : chartDto.getParameterDtos()) {

            ParameterDataDto indicatorDataDto = new ParameterDataDto();
            indicatorDto.setIndicatorType("NUMBER");
            indicatorDataDto.setParameterDto(indicatorDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();
            if (chartDto.getStartTime().equals(chartDto.getEndTime())) {
                chartDto1.setStartTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                chartDto1.setEndTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                // Query Data point
                for (int j = 0; j < 2; j++) {
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                            .minus(Duration.of(j * 10, ChronoUnit.MINUTES)));
                    Random generator = new Random();
                    dataPointDto.setValue(String.valueOf(generator.nextInt((30 - 10) + 1) + 10));
                    // dataPointDto.setLat(generator.nextDouble() * 360.0);
                    // dataPointDto.setLon(generator.nextDouble() * 360.0);
                    dataPointDtos.add(dataPointDto);
                }
            } else {
                // Data series
                chartDto1.setStartTime(chartDto.getStartTime());
                chartDto1.setEndTime(chartDto.getEndTime());
                Timestamp endTimestamp = Timestamp.valueOf(chartDto.getStartTime());
                ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                while (endTimestamp.before(Timestamp.valueOf(chartDto.getEndTime()))) {
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    endTimestamp = new Timestamp(
                            endTimestamp.getTime() + (int) (chartResolution.getValueFromEnum() * 3600000));
                    Random generator = new Random();
                    dataPointDto.setValue(String.valueOf(generator.nextInt((30 - 10) + 1) + 10));
                    dataPointDtos.add(dataPointDto);

                }

            }
//            indicatorDataDto.setDataPointDtos(Flux.just(dataPointDtos));
            indicatorDataDtos.add(indicatorDataDto);

        }
        chartDto1.setParameterDatas(indicatorDataDtos);


        // Call data from chartQuery

        return chartDto1;
    }

    public ChartDto allData( ChartDto chartDto) throws  Exception{
        ChartDto chartResponse = new ChartDto();
        if (chartDto.getStartTime().isAfter(chartDto.getEndTime())) {
            throw new Exception("Invalid Time Range");
        }
        chartResponse.setStartTime(chartDto.getStartTime());
        chartResponse.setEndTime(chartDto.getEndTime());
        List<ParameterDataDto> parameterDataDtos = new ArrayList<>();
        for (ParameterDto parameterDto : chartDto.getParameterDtos()) {
            ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId())
                    .orElseThrow(()-> new Exception("Invalid Parameter!"));
            Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId()).orElseThrow(()-> new Exception("Invalid Indicator"));
            ParameterDataDto parameterDataDto = new ParameterDataDto();
            parameterDto.setIndicatorType(indicator.getValueType());
            parameterDto.setIndicatorName(indicator.getIndicatorName());
            parameterDto.setUnit(indicator.getStandardUnit());
            parameterDto.setParameterName(indicator.getIndicatorName());
            parameterDataDto.setParameterDto(parameterDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();
            if (indicator.getValueType().equals(ConfigData.NUMBER.toString())){

                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateNumericData(numericalTsByOrgs,ChartResolution.DEFAULT, new ArrayList<>());
                }
                else {
                    ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                    List<Alarm> alarms = alarmRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId());
                    if (chartResolution.equals(ChartResolution.DEFAULT) || chartResolution.equals(ChartResolution.MIN_30) || chartResolution.equals(ChartResolution.HOUR_1)
                            || chartResolution.equals(ChartResolution.HOUR_2) || chartResolution.equals(ChartResolution.HOUR_4) || chartResolution.equals(ChartResolution.HOUR_8)) {
                        List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());


//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(numericalTsByOrgs.size()>0) {
//                            Collections.sort(numericalTsByOrgs, reverseComparator);
                            dataPointDtos = calculateNumericData(numericalTsByOrgs, chartResolution, alarms);
                        }
                    } else {
                        List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(numericalStatByOrgs.size()>0) {
//                            numericalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calulateNumericDataDate(numericalStatByOrgs, chartResolution,chartDto.getStartTime().toLocalDate(), alarms);
                        }
                    }
                }


            }else{
                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateCategoricalData(categoricalTsByOrgs,ChartResolution.DEFAULT);
                }else {
                    ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                    if (chartResolution.equals(ChartResolution.DEFAULT) || chartResolution.equals(ChartResolution.MIN_30) || chartResolution.equals(ChartResolution.HOUR_1)
                            || chartResolution.equals(ChartResolution.HOUR_2) || chartResolution.equals(ChartResolution.HOUR_4) || chartResolution.equals(ChartResolution.HOUR_8)) {
                        List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(categoricalTsByOrgs.size()>0) {
//                            Collections.sort(categoricalTsByOrgs, reverseComparator1);
                            dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, chartResolution);
                        }
                    } else {
                        List<CategoricalStatByOrg> categoricalStatByOrgs = categoricalStatByOrgRepository.findCatelogicalDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(categoricalStatByOrgs.size()>0) {
                            categoricalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calulateCatelogicalDataDate(categoricalStatByOrgs, chartResolution,chartDto.getStartTime().toLocalDate());
                        }
                    }
                }
            }
            parameterDataDto.setDataPointDtos(dataPointDtos);


            parameterDataDtos.add(parameterDataDto);
        }
        chartResponse.setParameterDatas(parameterDataDtos);
        return chartResponse;
    }

    Comparator<NumericalTsByOrg> reverseComparator = (c1, c2) -> {
        return c2.getPartitionKey().getEventTime().compareTo(c1.getPartitionKey().getEventTime());
    };
    Comparator<CategoricalTsByOrg> reverseComparator1 = (c1, c2) -> {
        return c2.getPartitionKey().getEventTime().compareTo(c1.getPartitionKey().getEventTime());
    };


    private List<DataPointDto> calculateNumericData(List<NumericalTsByOrg> numericalTsByOrgs, ChartResolution chartResolution, List<Alarm> alarms){
//        Collections.reverse(numericalTsByOrgs);
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if (chartResolution.equals(ChartResolution.DEFAULT)){
                return numericalTsByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
        Map<Object, List<NumericalTsByOrg>> dataGroup = numericalTsByOrgs.stream().collect(Collectors.groupingBy(e -> {
            int minutes = e.getPartitionKey().getEventTime().getMinute();
            int minutesOver = minutes % (int)(chartResolution.getValueFromEnum()*60);
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
            dataPointDtoList.add(dataPointDto);
        }
        dataPointDtoList.sort(Comparator.comparing(o -> o.getEventTime()));
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

    private List<DataPointDto> calulateNumericDataDate(List<NumericalStatByOrg> numericalStatByOrgs, ChartResolution chartResolution, LocalDate sDate, List<Alarm> alarms){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(chartResolution.equals(ChartResolution.DAY_1)){
            return  numericalStatByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
//        LocalDate sDate = numericalStatByOrgs.get(0).getPartitionKey().getDate();
        Map<Object, List<NumericalStatByOrg>> groupDate = numericalStatByOrgs.stream().collect(Collectors.groupingBy(event ->
                Math.floor((ChronoUnit.DAYS.between(sDate, event.getPartitionKey().getDate()))/(chartResolution.getValueFromEnum()/24))));
        for (Map.Entry<Object, List<NumericalStatByOrg>> entry: groupDate.entrySet()){
            Double sum = 0d;
            Double max = 0d;
            Double min = Double.MAX_VALUE;
            Long cnt = 0l;
            for (NumericalStatByOrg numericalStatByOrg: entry.getValue()){
                if(numericalStatByOrg.getMax()>max) max = numericalStatByOrg.getMax();
                if(numericalStatByOrg.getMin() <= min) min =numericalStatByOrg.getMin();
                sum += numericalStatByOrg.getMean();
                cnt += 1;
            }
            Double median = Comparison.median(entry.getValue().stream().map(NumericalStatByOrg::getMedian).collect(Collectors.toList()));
            DataPointDto dataPointDto = new DataPointDto();
            dataPointDto.setEventTime(sDate.plusDays(Math.round((Double)entry.getKey() * (chartResolution.getValueFromEnum()/24))).atStartOfDay());
            dataPointDto.setValue(Comparison.roundNum(sum/cnt,2).toString());
            for (Alarm alarm: alarms){
                if(Comparison.checkMatchingAlarmCondition(alarm,Comparison.roundNum(sum/cnt,2))){
                    dataPointDto.setIsAlarm(Boolean.TRUE);
                    dataPointDto.setAlarmType(alarm.getAlarmType().toString());
                    dataPointDto.setAlarmId(alarm.getPartitionKey().getAlarmId());
                    break;
                }
            }
            dataPointDto.setCount(cnt);
            dataPointDto.setMax(max.toString());
            dataPointDto.setMin(min.toString());
            dataPointDto.setMedian(median.toString());
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

    private List<DataPointDto> calculateCategoricalData(List<CategoricalTsByOrg> categoricalTsByOrgs, ChartResolution chartResolution){
//        Collections.reverse(categoricalTsByOrgs);
        if (chartResolution.equals(ChartResolution.DEFAULT)){
                return categoricalTsByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
        List<DataPointDto> dataPointDtoList = new ArrayList<>();

        Map<Object, List<CategoricalTsByOrg>> dataGroup = categoricalTsByOrgs.stream().collect(Collectors.groupingBy(e -> {
            int minutes = e.getPartitionKey().getEventTime().getMinute();
            int minutesOver = minutes % (int)(chartResolution.getValueFromEnum()*60);
            return e.getPartitionKey().getEventTime().truncatedTo(ChronoUnit.MINUTES).withMinute(minutes - minutesOver);
        }));
        for (Map.Entry<Object, List<CategoricalTsByOrg>> entry: dataGroup.entrySet()){
            Map<String, List<CategoricalTsByOrg>> groupDataByValue = entry.getValue().stream()
                    .collect(Collectors.groupingBy(event->event.getPartitionKey().getValue()));
            for (Map.Entry<String, List<CategoricalTsByOrg>> subEntry: groupDataByValue.entrySet()){
                DataPointDto dataPointDto = new DataPointDto();
                dataPointDto.setValue(subEntry.getKey());
                dataPointDto.setCount((long) subEntry.getValue().size());
                dataPointDto.setEventTime((LocalDateTime)entry.getKey());
                dataPointDtoList.add(dataPointDto);
            }
        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
//        Timestamp sTime =  Timestamp.valueOf(categoricalTsByOrgs.get(0).getPartitionKey().getEventTime());
//        Long rangeTime = 0l;
//        Long count = 0l;
//        dataPointDtoList.add(new DataPointDto(categoricalTsByOrgs.get(0)));
//        for (CategoricalTsByOrg categoricalTsByOrg: categoricalTsByOrgs){
//            rangeTime = Timestamp.valueOf(categoricalTsByOrg.getPartitionKey().getEventTime()).getTime() - sTime.getTime();
//
//            count +=1;
//
//            if (chartResolution.equals(ChartResolution.DEFAULT)){
//                return categoricalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
//            }else {
//                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
//                    DataPointDto dataPointDto = new DataPointDto();
//                    dataPointDto.setEventTime(categoricalTsByOrg.getPartitionKey().getEventTime());
//                    dataPointDto.setValue(categoricalTsByOrg.getPartitionKey().getValue());
//                    dataPointDtoList.add(dataPointDto);
//                    sTime = Timestamp.valueOf(categoricalTsByOrg.getPartitionKey().getEventTime());
//                    count = 0L;
//                }
//            }
//
//        }
//
//        DataPointDto dataPointDto = new DataPointDto();
//        if (count>0) {
//            dataPointDto.setEventTime(categoricalTsByOrgs.get(categoricalTsByOrgs.size() - 1).getPartitionKey().getEventTime());
//            dataPointDto.setValue(categoricalTsByOrgs.get(categoricalTsByOrgs.size()-1).getPartitionKey().getValue());
//            dataPointDtoList.add(dataPointDto);
//        }
        return dataPointDtoList;
    }

    private List<DataPointDto> calulateCatelogicalDataDate(List<CategoricalStatByOrg> categoricalStatByOrgs, ChartResolution chartResolution,  LocalDate sDate){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(chartResolution.equals(ChartResolution.DAY_1)){
            return categoricalStatByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
//        LocalDate sDate = categoricalStatByOrgs.get(0).getPartitionKey().getDate();
        Map<Object, List<CategoricalStatByOrg>> groupDate = categoricalStatByOrgs.stream().collect(Collectors.groupingBy(event ->
                Math.floor((ChronoUnit.DAYS.between(sDate, event.getPartitionKey().getDate()))/(chartResolution.getValueFromEnum()/24))));
        for (Map.Entry<Object, List<CategoricalStatByOrg>> entry: groupDate.entrySet()){
            Map<String, List<CategoricalStatByOrg>> groupDataByValue = entry.getValue().stream()
                    .collect(Collectors.groupingBy(event->event.getPartitionKey().getValue()));
            for (Map.Entry<String, List<CategoricalStatByOrg>> subEntry: groupDataByValue.entrySet()){
                DataPointDto dataPointDto = new DataPointDto();
                dataPointDto.setValue(subEntry.getKey());
                dataPointDto.setCount((long) subEntry.getValue().size());
                dataPointDto.setEventTime(sDate.plusDays(Math.round((Double)entry.getKey() * (chartResolution.getValueFromEnum()/24))).atStartOfDay());
                dataPointDtoList.add(dataPointDto);
            }


        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
//        else{
//            Timestamp sTime =  Timestamp.valueOf(categoricalStatByOrgs.get(0).getPartitionKey().getDate().atStartOfDay());
//            Long rangeTime = 0l;
//
//            Long count = 0l;
//            dataPointDtoList.add(new DataPointDto(categoricalStatByOrgs.get(0)));
//            for (CategoricalStatByOrg categoricalStatByOrg: categoricalStatByOrgs){
//                rangeTime = Timestamp.valueOf(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay()).getTime() - sTime.getTime();
//
//                count +=1;
//                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
//                    DataPointDto dataPointDto = new DataPointDto();
//                    dataPointDto.setEventTime(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay());
//                    dataPointDto.setValue(categoricalStatByOrg.getPartitionKey().getValue());
//                    dataPointDtoList.add(dataPointDto);
//                    sTime = Timestamp.valueOf(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay());
//                    count = 0L;
//                }
//            }
//            DataPointDto dataPointDto = new DataPointDto();
//            if (count>0) {
//                dataPointDto.setEventTime(categoricalStatByOrgs.get(categoricalStatByOrgs.size() - 1).getPartitionKey().getDate().atStartOfDay());
//                dataPointDto.setValue(categoricalStatByOrgs.get(categoricalStatByOrgs.size() - 1).getPartitionKey().getValue());
//                dataPointDtoList.add(dataPointDto);
//            }

//        }
        return  dataPointDtoList;
    }


    public  ApiResponse getAlarmData(ChartDto chartDto) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        ChartDto chartResponse = new ChartDto();
        if (chartDto.getStartTime().isAfter(chartDto.getEndTime())) {
            throw new Exception("Invalid Time Range");
        }
        chartResponse.setStartTime(chartDto.getStartTime());
        chartResponse.setEndTime(chartDto.getEndTime());
        List<ParameterDataDto> parameterDataDtos = new ArrayList<>();
        for (ParameterDto parameterDto : chartDto.getParameterDtos()) {
            ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId())
                    .orElseThrow(()-> new Exception("Invalid Parameter!"));
            Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId()).orElseThrow(()-> new Exception("Invalid Indicator"));
            ParameterDataDto parameterDataDto = new ParameterDataDto();
            parameterDto.setIndicatorType(indicator.getValueType());
            parameterDto.setIndicatorName(indicator.getIndicatorName());
            parameterDto.setUnit(indicator.getStandardUnit());
            parameterDto.setParameterName(indicator.getIndicatorName());
            parameterDataDto.setParameterDto(parameterDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();
            if (indicator.getValueType().equals(ConfigData.NUMBER.toString())){

                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateNumericData(numericalTsByOrgs,ChartResolution.DEFAULT, new ArrayList<>());
                }
                else {
                    ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                    List<Alarm> alarms = alarmRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    if (chartResolution.equals(ChartResolution.DEFAULT) || chartResolution.equals(ChartResolution.MIN_30) || chartResolution.equals(ChartResolution.HOUR_1)
                            || chartResolution.equals(ChartResolution.HOUR_2) || chartResolution.equals(ChartResolution.HOUR_4) || chartResolution.equals(ChartResolution.HOUR_8)) {
                        List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());

                        if(numericalTsByOrgs.size()>0) {
//                            Collections.sort(numericalTsByOrgs, reverseComparator);
                            dataPointDtos = calculateNumericData(numericalTsByOrgs, chartResolution, alarms).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());
                        }
                    } else {
                        List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(numericalStatByOrgs.size()>0) {
//                            numericalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calulateNumericDataDate(numericalStatByOrgs, chartResolution,chartDto.getStartTime().toLocalDate(), alarms).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());
                        }
                    }
                }


            }else{
                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateCategoricalData(categoricalTsByOrgs,ChartResolution.DEFAULT).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());;
                }else {
                    ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                    if (chartResolution.equals(ChartResolution.DEFAULT) || chartResolution.equals(ChartResolution.MIN_30) || chartResolution.equals(ChartResolution.HOUR_1)
                            || chartResolution.equals(ChartResolution.HOUR_2) || chartResolution.equals(ChartResolution.HOUR_4) || chartResolution.equals(ChartResolution.HOUR_8)) {
                        List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(categoricalTsByOrgs.size()>0) {
//                            Collections.sort(categoricalTsByOrgs, reverseComparator1);
                            dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, chartResolution).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());

                        }
                    } else {
                        List<CategoricalStatByOrg> categoricalStatByOrgs = categoricalStatByOrgRepository.findCatelogicalDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(categoricalStatByOrgs.size()>0) {
                            categoricalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calulateCatelogicalDataDate(categoricalStatByOrgs, chartResolution,chartDto.getStartTime().toLocalDate()).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());

                        }
                    }
                }
            }
            parameterDataDto.setDataPointDtos(dataPointDtos);


            parameterDataDtos.add(parameterDataDto);
        }
        chartResponse.setParameterDatas(parameterDataDtos);
        apiResponse.setMessage("successful!");
        apiResponse.setData(chartResponse);

        return apiResponse;
    }


}
