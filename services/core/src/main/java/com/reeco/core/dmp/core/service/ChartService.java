package com.reeco.core.dmp.core.service;


import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Duration;
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
        List<ParameterDataDto> parameterDataDtos = new ArrayList<>();
        for (ParameterDto parameterDto : chartDto.getParameterDtos()) {
            ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(),parameterDto.getParameterId())
                    .orElseThrow(()-> new Exception("Invalid Parameter!"));
            Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId()).orElseThrow(()-> new Exception("Invalid Indicator"));
            ParameterDataDto parameterDataDto = new ParameterDataDto();
            parameterDto.setIndicatorType(indicator.getValueType());
            parameterDto.setUnit(indicator.getUnit());
            parameterDto.setParameterName(indicator.getIndicatorName());
            parameterDataDto.setParameterDto(parameterDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();
            if (indicator.getValueType().equals("number")){

                if(chartDto.getStartTime().equals(chartDto.getEndTime())){
                    List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.find2LatestRow(parameterDto.getOrganizationId(), parameterDto.getParameterId());
                    dataPointDtos = calculateNumericData(numericalTsByOrgs,ChartResolution.DEFAULT);
                }
                else {
                    ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                    if (chartResolution.equals(ChartResolution.DEFAULT) || chartResolution.equals(ChartResolution.MIN_30) || chartResolution.equals(ChartResolution.HOUR_1)
                            || chartResolution.equals(ChartResolution.HOUR_2) || chartResolution.equals(ChartResolution.HOUR_4) || chartResolution.equals(ChartResolution.HOUR_8)) {
                        List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                                Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
//                  dataPointDtos = numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
                        Collections.sort(numericalTsByOrgs, reverseComparator);
                        dataPointDtos = calculateNumericData(numericalTsByOrgs, chartResolution);
                    } else {
                        List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        numericalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                        dataPointDtos = calulateNumericDataDate(numericalStatByOrgs, chartResolution);

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
                        dataPointDtos = calculateCategoricalData(categoricalTsByOrgs, chartResolution);
                    } else {
                        List<CategoricalStatByOrg> categoricalStatByOrgs = categoricalStatByOrgRepository.findCatelogicalDataDate(parameterDto.getOrganizationId(),
                                parameterDto.getParameterId(), chartDto.getStartTime().toLocalDate(), chartDto.getEndTime().toLocalDate());
                        categoricalStatByOrgs.sort(Comparator.comparing(o -> o.getPartitionKey().getDate()));
                        dataPointDtos = calulateCatelogicalDataDate(categoricalStatByOrgs, chartResolution);

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


    private List<DataPointDto> calculateNumericData(List<NumericalTsByOrg> numericalTsByOrgs, ChartResolution chartResolution){
        Collections.reverse(numericalTsByOrgs);
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        Timestamp sTime =  Timestamp.valueOf(numericalTsByOrgs.get(0).getPartitionKey().getEventTime());
        Long rangeTime = 0l;
        Double sumValue =0d;
        Long count = 0l;
        dataPointDtoList.add(new DataPointDto(numericalTsByOrgs.get(0)));
        for (NumericalTsByOrg numericalTsByOrg: numericalTsByOrgs){
            rangeTime = Timestamp.valueOf(numericalTsByOrg.getPartitionKey().getEventTime()).getTime() - sTime.getTime();
            sumValue += numericalTsByOrg.getValue();
            count +=1;

            if (chartResolution.equals(ChartResolution.DEFAULT)){
                return numericalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
            }else {
                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(numericalTsByOrg.getPartitionKey().getEventTime());
                    Double value = sumValue/count;
                    dataPointDto.setValue(value.toString());
                    dataPointDtoList.add(dataPointDto);
                    sTime = Timestamp.valueOf(numericalTsByOrg.getPartitionKey().getEventTime());
                    sumValue = 0D;
                    count = 0L;
                }
            }

        }

        DataPointDto dataPointDto = new DataPointDto();
        if (count>0) {
            dataPointDto.setEventTime(numericalTsByOrgs.get(numericalTsByOrgs.size() - 1).getPartitionKey().getEventTime());
            Double value = sumValue / count;
            dataPointDto.setValue(value.toString());
            dataPointDtoList.add(dataPointDto);
        }
//        dataPointDtoList.add(new DataPointDto(numericalTsByOrgs.get(numericalTsByOrgs.size()-1)));
        return dataPointDtoList;
    }

    private List<DataPointDto> calulateNumericDataDate(List<NumericalStatByOrg> numericalStatByOrgs, ChartResolution chartResolution){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(chartResolution.equals(ChartResolution.DAY_1)){
            return numericalStatByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
        }else{
            Timestamp sTime =  Timestamp.valueOf(numericalStatByOrgs.get(0).getPartitionKey().getDate().atStartOfDay());
            Long rangeTime = 0l;
            Double sumValue =0d;
            Long count = 0l;
            dataPointDtoList.add(new DataPointDto(numericalStatByOrgs.get(0)));
            for (NumericalStatByOrg numericalStatByOrg: numericalStatByOrgs){
                rangeTime = Timestamp.valueOf(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay()).getTime() - sTime.getTime();
                sumValue += numericalStatByOrg.getMean();
                count +=1;
                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay());
                    Double value = sumValue/count;
                    dataPointDto.setValue(value.toString());
                    dataPointDtoList.add(dataPointDto);
                    sTime = Timestamp.valueOf(numericalStatByOrg.getPartitionKey().getDate().atStartOfDay());
                    sumValue = 0D;
                    count = 0L;
                }
            }
            DataPointDto dataPointDto = new DataPointDto();
            if (count>0) {
                dataPointDto.setEventTime(numericalStatByOrgs.get(numericalStatByOrgs.size() - 1).getPartitionKey().getDate().atStartOfDay());
                Double value = sumValue / count;
                dataPointDto.setValue(value.toString());
                dataPointDtoList.add(dataPointDto);
            }

        }
        return  dataPointDtoList;
    }

    private List<DataPointDto> calculateCategoricalData(List<CategoricalTsByOrg> categoricalTsByOrgs, ChartResolution chartResolution){
        Collections.reverse(categoricalTsByOrgs);
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        Timestamp sTime =  Timestamp.valueOf(categoricalTsByOrgs.get(0).getPartitionKey().getEventTime());
        Long rangeTime = 0l;
        Long count = 0l;
        dataPointDtoList.add(new DataPointDto(categoricalTsByOrgs.get(0)));
        for (CategoricalTsByOrg categoricalTsByOrg: categoricalTsByOrgs){
            rangeTime = Timestamp.valueOf(categoricalTsByOrg.getPartitionKey().getEventTime()).getTime() - sTime.getTime();

            count +=1;

            if (chartResolution.equals(ChartResolution.DEFAULT)){
                return categoricalTsByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
            }else {
                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(categoricalTsByOrg.getPartitionKey().getEventTime());
                    dataPointDto.setValue(categoricalTsByOrg.getPartitionKey().getValue());
                    dataPointDtoList.add(dataPointDto);
                    sTime = Timestamp.valueOf(categoricalTsByOrg.getPartitionKey().getEventTime());
                    count = 0L;
                }
            }

        }

        DataPointDto dataPointDto = new DataPointDto();
        if (count>0) {
            dataPointDto.setEventTime(categoricalTsByOrgs.get(categoricalTsByOrgs.size() - 1).getPartitionKey().getEventTime());
            dataPointDto.setValue(categoricalTsByOrgs.get(categoricalTsByOrgs.size()-1).getPartitionKey().getValue());
            dataPointDtoList.add(dataPointDto);
        }
        return dataPointDtoList;
    }

    private List<DataPointDto> calulateCatelogicalDataDate(List<CategoricalStatByOrg> categoricalStatByOrgs, ChartResolution chartResolution){
        List<DataPointDto> dataPointDtoList = new ArrayList<>();
        if(chartResolution.equals(ChartResolution.DAY_1)){
            return categoricalStatByOrgs.stream().map(DataPointDto::new).collect(Collectors.toList());
        }else{
            Timestamp sTime =  Timestamp.valueOf(categoricalStatByOrgs.get(0).getPartitionKey().getDate().atStartOfDay());
            Long rangeTime = 0l;

            Long count = 0l;
            dataPointDtoList.add(new DataPointDto(categoricalStatByOrgs.get(0)));
            for (CategoricalStatByOrg categoricalStatByOrg: categoricalStatByOrgs){
                rangeTime = Timestamp.valueOf(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay()).getTime() - sTime.getTime();

                count +=1;
                if (rangeTime >= (chartResolution.getValueFromEnum()*3600000L)){
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay());
                    dataPointDto.setValue(categoricalStatByOrg.getPartitionKey().getValue());
                    dataPointDtoList.add(dataPointDto);
                    sTime = Timestamp.valueOf(categoricalStatByOrg.getPartitionKey().getDate().atStartOfDay());
                    count = 0L;
                }
            }
            DataPointDto dataPointDto = new DataPointDto();
            if (count>0) {
                dataPointDto.setEventTime(categoricalStatByOrgs.get(categoricalStatByOrgs.size() - 1).getPartitionKey().getDate().atStartOfDay());
                dataPointDto.setValue(categoricalStatByOrgs.get(categoricalStatByOrgs.size() - 1).getPartitionKey().getValue());
                dataPointDtoList.add(dataPointDto);
            }

        }
        return  dataPointDtoList;
    }

}
