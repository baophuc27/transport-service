package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
import com.reeco.core.dmp.core.until.ApiResponse;
import com.reeco.core.dmp.core.until.Comparison;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
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


    public ResponseMessage recieveDataCsv(MultipartFile file, Long orgId, Long stationId, List<ParameterDto> parameterDtoList) throws Exception{
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
                    NumericalStatByOrg numericalStatByOrg = new NumericalStatByOrg(numericalStatByOrgKey, stats.getMin(), stats.getMax(),
                            Comparison.roundNum(Comparison.median(entry.getValue()), 2),
                            Comparison.roundNum(stats.getAverage(), 2), stats.getSum(),
                            Comparison.roundNum(Comparison.std(entry.getValue(), stats.getAverage()), 2),
                            stats.getCount(), LocalDateTime.now());
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
//                numHasSet
//            cateHasSet
//        numericalStatByOrgs

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
        csvBody.add(Arrays.asList("Time event", "Value", "Param", "Indicator", "Unit", "Lat","Lon"));
        for (ParameterDto parameterDto : chartDto.getParameterDtos()) {
            ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(parameterDto.getOrganizationId(), parameterDto.getParameterId())
                    .orElseThrow(() -> new Exception("Invalid Parameter!"));
            Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId()).orElseThrow(() -> new Exception("Invalid Indicator"));
            if(chartDto.getStartTime().isBefore(chartDto.getEndTime())){
                List<NumericalTsByOrg> numericalTsByOrgs = numericalTsByOrgRepository.findDataDetail(Timestamp.valueOf(chartDto.getStartTime()),
                        Timestamp.valueOf(chartDto.getEndTime()), parameterDto.getOrganizationId(), parameterDto.getParameterId());
                for (NumericalTsByOrg numericalTsByOrg: numericalTsByOrgs){
                    List<String> rowData = new ArrayList<>();
                    rowData.add(numericalTsByOrg.getPartitionKey().getEventTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    rowData.add(numericalTsByOrg.getValue().toString());
                    rowData.add(paramsByOrg.getParamName());
                    rowData.add(indicator.getIndicatorName());
                    rowData.add(indicator.getStandardUnit());
                    rowData.add((numericalTsByOrg.getLat()!= null)? numericalTsByOrg.getLat().toString() : null);
                    rowData.add((numericalTsByOrg.getLon()!= null)? numericalTsByOrg.getLon().toString() : null);
                    csvBody.add(rowData);
                }


            }

        }

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

    public ApiResponse getLatestDataConnection(Long orgId, List<Long> connectionIds) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        LatestData latestData = new LatestData();
        latestData.setOrganizationId(orgId);
        List<LatestDataConnection> latestDataConnections = new ArrayList<>();
        for (Long id: connectionIds){
            LatestDataConnection latestDataConnection = new LatestDataConnection();
            latestDataConnection.setConnectionId(id);
            List<ParamsByOrg> paramsByOrgs = paramsByOrgRepository.findParamByConnection(orgId, id);
            for (ParamsByOrg paramsByOrg: paramsByOrgs){
                NumericalTsByOrg numericalTsByOrg = numericalTsByOrgRepository.find1LatestRow(orgId,paramsByOrg.getPartitionKey().getParamId())
                        .orElseThrow(()-> new Exception("invalid connectionId!"));
                if (latestDataConnection.getLatestTime() == null){
                    latestDataConnection.setLatestTime(numericalTsByOrg.getPartitionKey().getEventTime());
                }else{
                    if(latestDataConnection.getLatestTime().isBefore(numericalTsByOrg.getPartitionKey().getEventTime())){
                        latestDataConnection.setLatestTime(numericalTsByOrg.getPartitionKey().getEventTime());
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



}
