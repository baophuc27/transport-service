package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.model.cassandra.*;
import com.reeco.core.dmp.core.repository.cassandra.*;
import com.reeco.core.dmp.core.utils.ApiResponse;
import com.reeco.core.dmp.core.utils.Comparison;
import com.reeco.core.dmp.core.utils.NumericAggregate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class DataService {

    @Autowired
    NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    CategoricalStatByOrgRepository categoricalStatByOrgRepository;

    @Autowired
    NumericalStatByOrgRepository numericalStatByOrgRepository;

    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;

    @Autowired
    ConnectionAlarmInfoRepository connectionAlarmInfoRepository;



    public ResponseMessage receiveDataCsv(MultipartFile file, Long orgId, Long stationId, List<ParameterDto> parameterDtoList) throws Exception{
            byte[] bytes = file.getBytes();

            ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream ));
            String line = "";
            Map<Long,List<Alarm>> alarms = new HashMap<>();
            List<NumericalTsByOrg> numericalTsByOrgs = new ArrayList<>();
            List<CategoricalTsByOrg> categoricalTsByOrgs = new ArrayList<>();
            List<NumericalStatByOrg> numericalStatByOrgs = new ArrayList<>();
            List<CategoricalStatByOrg> categoricalStatByOrgs = new ArrayList<>();
            List<ParameterDto> parameterDtos =new ArrayList<>();
            HashMap<String, List<Double>> numHasSet = new HashMap<>();
            HashMap<String, Integer> cateHasSet = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] listLine = line.split(",");

                if (listLine[0].equals("event_time")) {
                    for (int j = 1; j < listLine.length-2; j++){
                        for (ParameterDto parameterDto: parameterDtoList){
                            if(listLine[j].equals(parameterDto.getColumnKey())){

                                ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(orgId,parameterDto.getParameterId())
                                        .orElseThrow(()->new Exception("Invalid Param!"));
                                Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId())
                                        .orElseThrow(()->new Exception("Invalid Indicator"));
                                List<Alarm> alarmList = alarmRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(orgId,paramsByOrg.getPartitionKey().getParamId());
                                alarms.put(paramsByOrg.getPartitionKey().getParamId(), alarmList);
                                parameterDto.setIndicatorType(indicator.getValueType());
                                parameterDto.setConnectionId(paramsByOrg.getConnectionId());
                                parameterDto.setParameterName(paramsByOrg.getParamName());
                                parameterDto.setIndicatorName(indicator.getIndicatorName());
                                parameterDto.setStationId(paramsByOrg.getStationId());
                                parameterDtos.add(parameterDto);
                            }
                        }
                    }
                    if(listLine.length -3 != parameterDtos.size()){
                        throw new Exception("Column not match param!");
                    }
                }else{
//                    System.out.println(listLine);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                    ZoneId timeZone = ZoneId.of("Asia/Ho_Chi_Minh");
//                    ZonedDateTime eTime = LocalDateTime.parse(listLine[0].substring(0,19), dtf).atZone(timeZone);

//                    ZonedDateTime vnTime = eTime.withZoneSameInstant(ZoneId.of("UTC"));
                    LocalDateTime event_time = LocalDateTime.parse(listLine[0].substring(0,19), dtf);
                    LocalDate date = event_time.toLocalDate();
                    Double lat = null;
                    Double lon = null;
                    if((listLine.length-2) == parameterDtos.size()){
                        lat = Double.parseDouble(listLine[listLine.length-1]);
                    }
                    if((listLine.length-3) == parameterDtos.size()){
                        lon = Double.parseDouble(listLine[listLine.length-2]);
                        lat = Double.parseDouble(listLine[listLine.length-1]);
                    }

                    for(int i = 1; i <= parameterDtos.size(); i++) {
                            if(listLine[i].equals("null")){
                                continue;
                            }
                            if(parameterDtos.get(i-1).getIndicatorType().equals(ConfigData.NUMBER.toString())) {
                                NumericalTsByOrg.Key nkey = new NumericalTsByOrg.Key(
                                        orgId,  event_time, parameterDtos.get(i - 1).getParameterId()
                                );
    //                        Optional<NumericalTsByOrg> numericalTsByOrgOld = numericalTsByOrgRepository.findByPartitionKey(nkey);
                                Double value = Double.parseDouble(listLine[i]);
                                Boolean isAlarm = Boolean.FALSE;
                                String alarmType = null;
                                Long alarmId = null;
                                for (Alarm alarm: alarms.get(parameterDtos.get(i - 1).getParameterId())){
                                    if(Comparison.checkMatchingAlarmCondition(alarm,value)){
                                        isAlarm = Boolean.TRUE;
                                        alarmType = alarm.getAlarmType().toString();
                                        alarmId = alarm.getPartitionKey().getAlarmId();
                                        break;
                                    }
                                }
                                NumericalTsByOrg numericalTsByOrg = new NumericalTsByOrg(
                                        nkey, parameterDtos.get(i-1).getIndicatorName(),parameterDtos.get(i-1).getParameterName(),
                                        date,parameterDtos.get(i-1).getStationId(), parameterDtos.get(i-1).getConnectionId(),
                                        Double.parseDouble(listLine[i]), event_time,isAlarm, alarmType, alarmId, lat, lon
                                );
    //                    numericalTsByOrgRepository.save(numericalTsByOrg);
    //                    break;
                                String key =date.toString()+","+numericalTsByOrg.getPartitionKey().getParamId().toString();
                                if(!numHasSet.containsKey(key)){
                                    List<Double> tempList = new ArrayList<>();
                                    tempList.add(numericalTsByOrg.getValue());
                                    numHasSet.put(key, tempList);
                                }else{
                                    numHasSet.get(key).add(numericalTsByOrg.getValue());
                                }
                                numericalTsByOrgs.add(numericalTsByOrg);
                            }else {
                                CategoricalTsByOrg.Key nkey = new CategoricalTsByOrg.Key(
                                        orgId,  event_time, parameterDtos.get(i - 1).getParameterId(), listLine[i]
                                );
    //
                                CategoricalTsByOrg categoricalTsByOrg = new CategoricalTsByOrg(
                                        nkey,  parameterDtos.get(i-1).getIndicatorName(), parameterDtos.get(i-1).getParameterName(),
                                        date,parameterDtos.get(i-1).getStationId(),false, null, null, parameterDtos.get(i-1).getConnectionId(), event_time, lat, lon
                                );
    //                    numericalTsByOrgRepository.save(numericalTsByOrg);
    //                    break;
                                String key =date.toString()+","+categoricalTsByOrg.getPartitionKey().getParamId().toString() + "," +
                                        categoricalTsByOrg.getPartitionKey().getValue();
                                if(!cateHasSet.containsKey(key)){
                                    Integer cnt = 1;
//                                    List<String> tempList = new ArrayList<>();
//                                    tempList.add(categoricalTsByOrg.getPartitionKey().getValue());
                                    cateHasSet.put(key, cnt);
                                }else{
                                    cateHasSet.put(key, cateHasSet.get(key) + 1);
                                }
                                categoricalTsByOrgs.add(categoricalTsByOrg);
                            }
                    }
                }
            }
            br.close();
            if(numHasSet.size()>0) {
                for (Map.Entry<String, List<Double>> entry : numHasSet.entrySet()) {
                    DoubleSummaryStatistics stats = entry.getValue().stream()
                            .mapToDouble((x) -> x)
                            .summaryStatistics();
                    String key = entry.getKey();
                    String[] dp = key.split(",");
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate numDate = LocalDate.parse(dp[0].split(" ")[0], df);
                    Long paramId = Long.parseLong(dp[1]);
                    NumericalStatByOrg.Key numericalStatByOrgKey = new NumericalStatByOrg.Key(orgId, numDate, paramId);
                    Double value = stats.getAverage();
                    Boolean isAlarm = Boolean.FALSE;
                    String alarmType = null;
                    Long alarmId = null;
                    for (Alarm alarm: alarms.get(paramId)){
                        if(Comparison.checkMatchingAlarmCondition(alarm,value)){
                            isAlarm = Boolean.TRUE;
                            alarmType = alarm.getAlarmType().toString();
                            alarmId = alarm.getPartitionKey().getAlarmId();
                            break;
                        }
                    }
                    NumericalStatByOrg numericalStatByOrg = new NumericalStatByOrg(numericalStatByOrgKey, stats.getMin(), stats.getMax(),
                            Comparison.roundNum(Comparison.median(entry.getValue()), 2),
                            Comparison.roundNum(stats.getAverage(), 2), stats.getSum(),
                            Comparison.roundNum(Comparison.std(entry.getValue(), stats.getAverage()), 2),
                            stats.getCount(), LocalDateTime.now(),isAlarm,  alarmId, alarmType);
                    numericalStatByOrgs.add(numericalStatByOrg);
                }
            }
            if(cateHasSet.size()>0){
                for (Map.Entry<String, Integer> entry: cateHasSet.entrySet()){
                    String key = entry.getKey();
                    String[] dp = key.split(",");
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate cateDate = LocalDate.parse(dp[0].split(" ")[0], df);
                    Long paramId = Long.parseLong(dp[1]);
                    String value = dp[2];
                    CategoricalStatByOrg.Key  cateKey= new CategoricalStatByOrg.Key(
                            orgId,cateDate,paramId,value
                    );
                    CategoricalStatByOrg categoricalStatByOrg = new CategoricalStatByOrg(
                            cateKey, entry.getValue().longValue(),LocalDateTime.now()
                    );
                    categoricalStatByOrgs.add(categoricalStatByOrg);

                }
            }
            numericalTsByOrgRepository.saveAll(numericalTsByOrgs);
            categoricalTsByOrgRepository.saveAll(categoricalTsByOrgs);
            numericalStatByOrgRepository.saveAll(numericalStatByOrgs);
            categoricalStatByOrgRepository.saveAll(categoricalStatByOrgs);

//            TimeUnit.SECONDS.sleep(5);
            return new ResponseMessage("Import data successful!");


    }

    public String exportDataCsv(@RequestBody ChartDto chartDto) throws Exception{
        String[] csvHeader = {
                "Data Export"
        };
        List<List<String>> csvBody = new ArrayList<>();
        ByteArrayInputStream byteArrayOutputStream = null;
        String encodedString = "";
        List<String> rowLabel = new ArrayList<>();
        rowLabel.add("Time event");

        HashMap<String, List<String>> response = new HashMap<>();
        int idx = 0;


        Resolution resolution = Resolution.valueOf(chartDto.getResolution());
        List<ParameterDataDto> parameterDataDtos = new ArrayList<>();

        for (ParameterDto parameterDto : chartDto.getParameterDtos()) {
//            log.info("Get data for: {}",parameterDto);
            ParamsByOrg paramsByOrg = paramsByOrgRepository
                    .findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(), parameterDto.getParameterId())
                    .orElseThrow(() -> new Exception("Invalid Parameter!"));
            Indicator indicator = indicatorInfoRepository
                    .findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId())
                    .orElseThrow(() -> new Exception("Invalid Indicator"));

            String standardUnit = indicator.getStandardUnit();
            String paramName = paramsByOrg.getParamName();

            // Aggregate
            ParameterDataDto parameterDataDto = new ParameterDataDto();
            parameterDataDto.setParameterDto(parameterDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();

            if(chartDto.getStartTime().isBefore(chartDto.getEndTime())){
                List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(
                        Timestamp.valueOf(chartDto.getStartTime()),
                        Timestamp.valueOf(chartDto.getEndTime()),
                        parameterDto.getOrganizationId(),
                        parameterDto.getParameterId()
                );

                List<Alarm> alarms = new ArrayList<>();

                if (resolution.equals(Resolution.DEFAULT)) {
                    rowLabel.add(standardUnit == null ? paramName : paramName + "(" + standardUnit + ")");
                    for (NumericalTsByOrg numericalTsByOrg : numericalTsByOrgs) {
                        List<String> data = new ArrayList<>();
                        String key = numericalTsByOrg.getPartitionKey()
                                .getEventTime()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        String numericalValue = numericalTsByOrg.getValue() != null ? numericalTsByOrg.getValue().toString() : "";
                        if (idx > 0) {
                            if (response.containsKey(key)) {
                                for (int i = response.get(key).size(); i <= idx; i++) {
                                    response.get(key).add(i == idx ? numericalValue : "");
                                }
                            } else {
                                for (int i = 0; i <= idx; i++) {
                                    data.add(i == idx ? numericalValue : "");
                                }
                                response.put(key, data);
                            }
                        } else {
                            data.add(numericalValue);
                            response.put(key, data);
                        }
                    }
                }
                // Aggregate
                else if (resolution.equals(Resolution.MIN_15) || resolution.equals(Resolution.MIN_30) || resolution.equals(Resolution.HOUR_1) ||
                        resolution.equals(Resolution.HOUR_2) || resolution.equals(Resolution.HOUR_4) ||
                        resolution.equals(Resolution.HOUR_8)) {

                    rowLabel.add(paramName);
                    if(numericalTsByOrgs.size()>0) {
                        dataPointDtos = NumericAggregate.calculateNumericData(numericalTsByOrgs, resolution, alarms, AggregateMethod.valueOf(chartDto.getAggregate()));
                    }
                } else {
                    List<NumericalStatByOrg> numericalStatByOrgs = numericalStatByOrgRepository.findNumericDataDate(
                            parameterDto.getOrganizationId(),
                            parameterDto.getParameterId(),
                            chartDto.getStartTime().toLocalDate(),
                            chartDto.getEndTime().toLocalDate()
                    );

                    rowLabel.add(paramName);
                    if(numericalStatByOrgs.size()>0) {
                        dataPointDtos = NumericAggregate.calculateNumericDataDate(numericalStatByOrgs, resolution, chartDto.getStartTime().toLocalDate(), alarms, AggregateMethod.valueOf(chartDto.getAggregate()));
                    }
                }
            }
            // Aggregate
            parameterDataDto.setDataPointDtos(dataPointDtos);
            parameterDataDtos.add(parameterDataDto);

            idx += 1;
        }

        if (resolution.equals(Resolution.DEFAULT)) {
            for (Map.Entry<String, List<String>> entry: response.entrySet()){
                List<String> rowData = new ArrayList<>();
                rowData.add(entry.getKey());
                rowData.addAll(entry.getValue());
                csvBody.add(rowData);
            }
            csvBody.sort((o1, o2) -> {
                // 0 is datetime column
                if (o1.get(0) == null || o2.get(0) == null)
                    return 0;
                return o1.get(0).compareTo(o2.get(0));
            });
        } else {
            // Cause groupby in database will miss some timestamp -> create a default Map and fill parameter's value by its index
            HashMap<String, List<String>> aggregatedDataMap = new HashMap<>();
            for (int i =0; i < parameterDataDtos.size();i++){
                ParameterDataDto parameterDataDto = parameterDataDtos.get(i);
                for (DataPointDto dataPointDto: parameterDataDto.getDataPointDtos()) {
                    String eventTimeKey = String.valueOf(Timestamp.valueOf(dataPointDto.getEventTime()));
                    if (!aggregatedDataMap.containsKey(eventTimeKey)) {
                        List<String> valueList = new ArrayList<>();
                        for (int j=0;j< parameterDataDtos.size();j++){
                            valueList.add("");
                        }
                        aggregatedDataMap.put(eventTimeKey, valueList);
                    }
                    List<String> valueList = aggregatedDataMap.get(eventTimeKey);
                    String value = getAggregatedNumeric(chartDto.getAggregate(), dataPointDto);
                    valueList.set(i, value);
                }
            }
            for (Map.Entry<String, List<String>> entry: aggregatedDataMap.entrySet()){
                List<String> rowData = new ArrayList<>();
                rowData.add(entry.getKey());
                rowData.addAll(entry.getValue());
                csvBody.add(rowData);
            }
            csvBody.sort((o1, o2) -> {
                // 0 is datetime column
                if (o1.get(0) == null || o2.get(0) == null)
                    return 0;
                return o1.get(0).compareTo(o2.get(0));
            });
        }

        csvBody.add(0, rowLabel);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(
                new PrintWriter(out),
                CSVFormat.DEFAULT.withHeader(csvHeader)
        );
        for (List<String> record : csvBody){
            csvPrinter.printRecord(record);
        }

        csvPrinter.flush();

        byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());

        InputStreamResource template = new InputStreamResource(byteArrayOutputStream);
        encodedString = Base64.getEncoder().encodeToString(template.getInputStream().readAllBytes());
        return encodedString;
    }
    private String getAggregatedNumeric(String aggregateMethod, DataPointDto dataPointDto){
        String value = "";
        switch (AggregateMethod.valueOf(aggregateMethod)) {
            case INTERPOLATED:
                value = dataPointDto.getInterpolated();
                break;
            case MAX:
                value = dataPointDto.getMax();
                break;
            case MIN:
                value = dataPointDto.getMin();
                break;
            case MEAN:
                value = dataPointDto.getMean();
                break;
            case MEDIAN:
                value = dataPointDto.getMedian();
                break;
            case COUNT:
                value = String.valueOf(dataPointDto.getCount());
                break;
            case SUM:
                value = dataPointDto.getSum();
                break;
            case RANGE:
                value = dataPointDto.getRange();
                break;
            case START:
                value = dataPointDto.getStart();
                break;
            case END:
                value = dataPointDto.getEnd();
                break;
            case DELTA:
                value = dataPointDto.getDelta();
                break;
            default:
                break;
        }
        return value;
    }
    public ApiResponse getLatestDataConnection(Long orgId, List<Long> connectionIds) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        LatestData latestData = new LatestData();
        latestData.setOrganizationId(orgId);
        List<LatestDataConnection> latestDataConnections = new ArrayList<>();
        for (Long id: connectionIds){
            LatestDataConnection latestDataConnection = new LatestDataConnection();
            latestDataConnection.setConnectionId(id);
            List<ParamsByOrg> paramsByOrgs = paramsByOrgRepository.findParamByConnection(orgId, id);
            if(paramsByOrgs.size() <= 0) {
                throw new Exception("Not found connection in organization");
            }
            for (ParamsByOrg paramsByOrg: paramsByOrgs){
                Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId())
                        .orElseThrow(()-> new Exception("Invalid Indicator Id"));
                if (indicator.getValueType().equals("NUMBER")) {
                    Optional<NumericalTsByOrg> numericalTsByOrg = numericalTsByOrgRepository.find1LatestRow(orgId, paramsByOrg.getPartitionKey().getParamId());
                    if (numericalTsByOrg.isPresent()) {
                        if (latestDataConnection.getLatestTime() == null) {
                            latestDataConnection.setLatestTime(numericalTsByOrg.get().getPartitionKey().getEventTime());
                        } else {
                            if (latestDataConnection.getLatestTime().isBefore(numericalTsByOrg.get().getPartitionKey().getEventTime())) {
                                latestDataConnection.setLatestTime(numericalTsByOrg.get().getPartitionKey().getEventTime());
                            }
                        }
                    }
                }else{
                    Optional<CategoricalTsByOrg> categoricalTsByOrg = categoricalTsByOrgRepository.find1LatestRow(orgId, paramsByOrg.getPartitionKey().getParamId());
                    if (categoricalTsByOrg.isPresent()) {
                        if (latestDataConnection.getLatestTime() == null) {
                            latestDataConnection.setLatestTime(categoricalTsByOrg.get().getPartitionKey().getEventTime());
                        } else {
                            if (latestDataConnection.getLatestTime().isBefore(categoricalTsByOrg.get().getPartitionKey().getEventTime())) {
                                latestDataConnection.setLatestTime(categoricalTsByOrg.get().getPartitionKey().getEventTime());
                            }
                        }
                    }
                }

            }
            latestDataConnections.add(latestDataConnection);
        }
        latestData.setConnectionList(latestDataConnections);
        apiResponse.setData(latestData);
        apiResponse.setMessage("Successful!");
        return apiResponse;
    }

    public ApiResponse getConnectionHistory(Long orgId, Long connectionId, String status, Integer startIndex, Integer endIndex, Long startTime, Long endTime, Long aLarmId){
//        if (!status.equals("CONNECTED") & status.equals("DISCONNECTED")){
//            ApiResponse apiResponse = ApiResponse.getFailureResponse("Invalid status");
//            return apiResponse;
//        }

        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
//        log.info(orgId);
//        log.info(connectionId);
        List<ConnectionAlarmInfo> historyConnection = connectionAlarmInfoRepository.findHistoryByConnection(orgId,connectionId);
        log.info(historyConnection.toString());
        List<ConnectionHistory> result = new ArrayList<>();
        Long index = 0L;
        for (ConnectionAlarmInfo info: historyConnection){
            Long timestamp = info.getAlarmTime().atZone(ZoneId.of("GMT+7")).toEpochSecond();
            index += 1;

            ConnectionHistory connectionHistory = new ConnectionHistory(
                    info.getPartitionKey().getOrganizationId(),
                    info.getPartitionKey().getConnectionId(),
                    Math.toIntExact(index),
                    timestamp,
                    info.getAlarmType(),
                    info.getConnectionProtocol().toString(),
                    info.getDescription()
            );

            if (info.getDeleted()!= null && info.getDeleted() == true){
                continue;
            }

            if (status != null && status.equals("CONNECTED")){
                if (!info.getAlarmType().toString().equals("CONNECTED")){
                    continue;
                }
            }

            if (status != null && status.equals("DISCONNECTED")){
                if (!info.getAlarmType().toString().equals("DISCONNECTED") & !info.getAlarmType().toString().equals("AUTHENTICATION_FAILED")){
                    continue;
                }
            }

            if (startTime != null && startTime > timestamp)
            {
                continue;
            }

            if (endTime != null && endTime < timestamp){
                continue;
            }

            result.add(connectionHistory);
        }

        // Reverse since query is order by alarm_time ASCENDING, while we need DESCENDING order
        Collections.reverse(result);

        List<ConnectionHistory> slicedResult = result;

        if (startIndex != null && endIndex != null){
            slicedResult = sliceArrayList(result, startIndex, endIndex);
        }


        apiResponse.setMessage("Successful!");
        apiResponse.setData(slicedResult);
        return apiResponse;
    }

    public static List<ConnectionHistory> sliceArrayList(List<ConnectionHistory> list, int fromIndex, int toIndex) {
        // Ensure fromIndex is not less than 0
        if (fromIndex < 0) {
            fromIndex = 0;
        }

        // Ensure toIndex is not greater than the size of the list
        if (toIndex > list.size()) {
            toIndex = list.size();
        }

        // Ensure fromIndex is not greater than toIndex
        if (fromIndex > toIndex) {
            fromIndex = toIndex;
        }

        return list.subList(fromIndex, toIndex);
    }

    public ApiResponse deleteConnectionHistory(Long orgId, Long connectionId, Long index){

        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        List<ConnectionAlarmInfo> historyConnection = connectionAlarmInfoRepository.findHistoryByConnection(orgId,connectionId);

        Long count = 0L;
        if (index == null){
            for (ConnectionAlarmInfo info : historyConnection){
                info.setDeleted(true);
                connectionAlarmInfoRepository.save(info);
            }
        } else {
            for (ConnectionAlarmInfo info : historyConnection) {
                count += 1;
                if (count == index){
                    info.setDeleted(true);
                    connectionAlarmInfoRepository.save(info);
                }
            }
        }
        apiResponse.setMessage("Successful!");
        return apiResponse;
    }

}
