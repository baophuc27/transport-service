package com.reeco.core.dmp.core.service;


import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
import com.reeco.core.dmp.core.until.ApiResponse;
import com.reeco.core.dmp.core.until.NumericAggregate;
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
                Resolution resolution = Resolution.valueOf(chartDto.getResolution());
                while (endTimestamp.before(Timestamp.valueOf(chartDto.getEndTime()))) {
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    endTimestamp = new Timestamp(
                            endTimestamp.getTime() + (int) (resolution.getValueFromEnum() * 3600000));
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
            ParamsByOrg paramsByOrg = paramsByOrgRepository
                    .findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId())
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
                    dataPointDtos = NumericAggregate.calculateNumericData(numericalTsByOrgs, Resolution.DEFAULT, new ArrayList<>(), null);
                }
                else {
                    Resolution resolution = Resolution.valueOf(chartDto.getResolution());
                    List<Alarm> alarms = alarmRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId());
                    if (resolution.equals(Resolution.DEFAULT) || resolution.equals(Resolution.MIN_30) ||
                        resolution.equals(Resolution.HOUR_1) || resolution.equals(Resolution.HOUR_2) ||
                        resolution.equals(Resolution.HOUR_4) || resolution.equals(Resolution.HOUR_8)) {

                        List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(
                                Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()),
                                parameterDto.getOrganizationId(),
                                parameterDto.getParameterId()
                        );
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(numericalTsByOrgs.size()>0) {
//                            Collections.sort(numericalTsByOrgs, reverseComparator);
                            dataPointDtos = NumericAggregate.calculateNumericData(numericalTsByOrgs, resolution, alarms, null);
                        }
                    } else {
                        List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(
                                parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(),
                                chartDto.getStartTime().toLocalDate(),
                                chartDto.getEndTime().toLocalDate()
                        );
                        if(numericalStatByOrgs.size()>0) {
//                            numericalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = NumericAggregate.calculateNumericDataDate(numericalStatByOrgs, resolution,chartDto.getStartTime().toLocalDate(), alarms, null);
                        }
                    }
                }


            } else {
                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, Resolution.DEFAULT);
                } else {
                    Resolution resolution = Resolution.valueOf(chartDto.getResolution());
                    if (resolution.equals(Resolution.DEFAULT) || resolution.equals(Resolution.MIN_30) || resolution.equals(
                            Resolution.HOUR_1)
                            || resolution.equals(Resolution.HOUR_2) || resolution.equals(Resolution.HOUR_4) || resolution.equals(
                            Resolution.HOUR_8)) {
                        List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(categoricalTsByOrgs.size()>0) {
//                            Collections.sort(categoricalTsByOrgs, reverseComparator1);
                            dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, resolution);
                        }
                    } else {
                        List<CategoricalStatByOrg> categoricalStatByOrgs = categoricalStatByOrgRepository.findCategoricalDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(categoricalStatByOrgs.size()>0) {
                            categoricalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calculateCategoricalDataDate(categoricalStatByOrgs,
                                    resolution,chartDto.getStartTime().toLocalDate());
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


    private List<DataPointDto> calculateCategoricalData(List<CategoricalTsByOrg> categoricalTsByOrgs, Resolution resolution){
//        Collections.reverse(categoricalTsByOrgs);
        if (resolution.equals(Resolution.DEFAULT)){
                return categoricalTsByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
        List<DataPointDto> dataPointDtoList = new ArrayList<>();

        Map<Object, List<CategoricalTsByOrg>> dataGroup = categoricalTsByOrgs.stream().collect(Collectors.groupingBy(e -> {
            int minutes = e.getPartitionKey().getEventTime().getMinute();
            int minutesOver = minutes % (int)(resolution.getValueFromEnum()*60);
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
//            } else {
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

    private List<DataPointDto> calculateCategoricalDataDate(List<CategoricalStatByOrg> categoricalStatByOrgs, Resolution resolution, LocalDate sDate){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(resolution.equals(Resolution.DAY_1)){
            return categoricalStatByOrgs.stream().map(DataPointDto::new).sorted(Comparator.comparing(DataPointDto::getEventTime)).collect(Collectors.toList());
        }
//        LocalDate sDate = categoricalStatByOrgs.get(0).getPartitionKey().getDate();
        Map<Object, List<CategoricalStatByOrg>> groupDate = categoricalStatByOrgs.stream().collect(Collectors.groupingBy(event ->
                Math.floor((ChronoUnit.DAYS.between(sDate, event.getPartitionKey().getDate()))/(resolution.getValueFromEnum()/24))));
        for (Map.Entry<Object, List<CategoricalStatByOrg>> entry: groupDate.entrySet()){
            Map<String, List<CategoricalStatByOrg>> groupDataByValue = entry.getValue().stream()
                    .collect(Collectors.groupingBy(event->event.getPartitionKey().getValue()));
            for (Map.Entry<String, List<CategoricalStatByOrg>> subEntry: groupDataByValue.entrySet()){
                DataPointDto dataPointDto = new DataPointDto();
                dataPointDto.setValue(subEntry.getKey());
                dataPointDto.setCount((long) subEntry.getValue().size());
                dataPointDto.setEventTime(sDate.plusDays(Math.round((Double)entry.getKey() * (resolution.getValueFromEnum()/24))).atStartOfDay());
                dataPointDtoList.add(dataPointDto);
            }


        }
        dataPointDtoList.sort(Comparator.comparing(DataPointDto::getEventTime));
//        else {
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
                    dataPointDtos = NumericAggregate.calculateNumericData(numericalTsByOrgs, Resolution.DEFAULT, new ArrayList<>(), null);
                }
                else {
                    Resolution resolution = Resolution.valueOf(chartDto.getResolution());
                    List<Alarm> alarms = alarmRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    if (resolution.equals(Resolution.DEFAULT) || resolution.equals(Resolution.MIN_30) || resolution.equals(
                            Resolution.HOUR_1)
                            || resolution.equals(Resolution.HOUR_2) || resolution.equals(Resolution.HOUR_4) || resolution.equals(
                            Resolution.HOUR_8)) {
                        List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());

                        if(numericalTsByOrgs.size()>0) {
//                            Collections.sort(numericalTsByOrgs, reverseComparator);
                            dataPointDtos = NumericAggregate.calculateNumericData(numericalTsByOrgs, resolution, alarms, null).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());
                        }
                    } else {
                        List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(numericalStatByOrgs.size()>0) {
//                            numericalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = NumericAggregate.calculateNumericDataDate(numericalStatByOrgs, resolution,chartDto.getStartTime().toLocalDate(), alarms, null).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());
                        }
                    }
                }


            } else {
                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, Resolution.DEFAULT).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());
                } else {
                    Resolution resolution = Resolution.valueOf(chartDto.getResolution());
                    if (resolution.equals(Resolution.DEFAULT) || resolution.equals(Resolution.MIN_30) || resolution.equals(
                            Resolution.HOUR_1)
                            || resolution.equals(Resolution.HOUR_2) || resolution.equals(Resolution.HOUR_4) || resolution.equals(
                            Resolution.HOUR_8)) {
                        List<CategoricalTsByOrg> categoricalTsByOrgs = categoricalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        if(categoricalTsByOrgs.size()>0) {
//                            Collections.sort(categoricalTsByOrgs, reverseComparator1);
                            dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, resolution).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());

                        }
                    } else {
                        List<CategoricalStatByOrg> categoricalStatByOrgs = categoricalStatByOrgRepository.findCategoricalDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        if(categoricalStatByOrgs.size()>0) {
                            categoricalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                            dataPointDtos = calculateCategoricalDataDate(categoricalStatByOrgs,
                                    resolution,chartDto.getStartTime().toLocalDate()).stream().filter(DataPointDto::getIsAlarm).collect(Collectors.toList());

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
