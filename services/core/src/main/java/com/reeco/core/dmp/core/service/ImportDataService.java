package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.ParameterDto;
import com.reeco.core.dmp.core.dto.ResponseMessage;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
import com.reeco.core.dmp.core.until.Comparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ImportDataService {

    @Autowired
    NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

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
            List<NumericalTsByOrg> numericalTsByOrgs = new ArrayList<>();
            List<CategoricalTsByOrg> categoricalTsByOrgs = new ArrayList<>();
            List<NumericalStatByOrg> numericalStatByOrgs = new ArrayList<>();
            List<CategoricalStatByOrg> categoricalStatByOrgs = new ArrayList<>();
            List<ParameterDto> parameterDtos =new ArrayList<>();
            HashMap<String, List<Double>> numHasSet = new HashMap<>();
            HashMap<String, List<String>> cateHasSet = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] listLine = line.split(",");

                if (listLine[0].equals("event_time")) {
                    for (int j = 1; j < listLine.length-2; j++){
                        for (ParameterDto parameterDto: parameterDtoList){
                            if(listLine[j].equals(parameterDto.getColumnKey())){

                                ParamsByOrg paramsByOrg = paramsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(orgId,parameterDto.getParameterId())
                                        .orElseThrow(()->new Exception("Invalid Param!"));
                                Indicator indicator = indicatorInfoRepository.findByPartitionKeyIndicatorId(paramsByOrg.getIndicatorId())
                                        .orElseThrow(()->new Exception("Invalid Params"));
                                parameterDto.setIndicatorType(indicator.getValueType());
                                parameterDto.setConnectionId(paramsByOrg.getConnectionId());
                                parameterDto.setParameterName(paramsByOrg.getParamName());
                                parameterDto.setIndicatorName(indicator.getIndicatorName());
                                parameterDtos.add(parameterDto);
                            }
                        }
                    }
                    if(listLine.length -3 != parameterDtos.size()){
                        throw new Exception("Column not match param!");
                    }
                }else{
                    System.out.println(listLine);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDate date = LocalDate.parse(listLine[0].split(" ")[0], df);
                    LocalDateTime event_time = LocalDateTime.parse(listLine[0].substring(0,19), dtf);
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
                            if(parameterDtos.get(i-1).getIndicatorType().equals("number")) {
                                NumericalTsByOrg.Key nkey = new NumericalTsByOrg.Key(
                                        orgId, date, event_time, parameterDtos.get(i - 1).getParameterId()
                                );
    //                        Optional<NumericalTsByOrg> numericalTsByOrgOld = numericalTsByOrgRepository.findByPartitionKey(nkey);
                                NumericalTsByOrg numericalTsByOrg = new NumericalTsByOrg(
                                        nkey, parameterDtos.get(i-1).getIndicatorName(),parameterDtos.get(i-1).getParameterName(),  stationId, 0L,
                                        Double.parseDouble(listLine[i]), event_time, lat, lon
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
                                        orgId, date, event_time, parameterDtos.get(i - 1).getParameterId(), listLine[i]
                                );
    //
                                CategoricalTsByOrg categoricalTsByOrg = new CategoricalTsByOrg(
                                        nkey,  parameterDtos.get(i-1).getIndicatorName(), parameterDtos.get(i-1).getParameterName(), stationId, 0L,
                                         event_time, lat, lon
                                );
    //                    numericalTsByOrgRepository.save(numericalTsByOrg);
    //                    break;
                                String key =date.toString()+","+categoricalTsByOrg.getPartitionKey().getParamId().toString();
                                if(!cateHasSet.containsKey(key)){
                                    List<String> tempList = new ArrayList<>();
                                    tempList.add(categoricalTsByOrg.getPartitionKey().getValue());
                                    cateHasSet.put(key, tempList);
                                }else{
                                    cateHasSet.get(key).add(categoricalTsByOrg.getPartitionKey().getValue());
                                }
                                categoricalTsByOrgs.add(categoricalTsByOrg);
                            }


                    }
                }
            }
            br.close();
            for (Map.Entry<String, List<Double>> entry: numHasSet.entrySet()){
                DoubleSummaryStatistics stats = entry.getValue().stream()
                        .mapToDouble((x) -> x)
                        .summaryStatistics();
                String key = entry.getKey();
                String[] dp = key.split(",");
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate numDate = LocalDate.parse(dp[0].split(" ")[0], df);
                Long paramId = Long.parseLong(dp[1]);
                NumericalStatByOrg.Key numericalStatByOrgKey = new NumericalStatByOrg.Key(orgId,numDate,paramId);
                NumericalStatByOrg numericalStatByOrg = new NumericalStatByOrg(numericalStatByOrgKey, stats.getMin(),stats.getMax(),
                        Comparison.roundNum(Comparison.median(entry.getValue()),2),
                        Comparison.roundNum(stats.getAverage(),2),0d,
                        Comparison.roundNum(Comparison.std(entry.getValue(), stats.getAverage()),2),
                        stats.getCount(),LocalDateTime.now());
                numericalStatByOrgs.add(numericalStatByOrg);
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




}
