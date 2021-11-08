package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.ParameterDto;
import com.reeco.core.dmp.core.dto.ResponseMessage;
import com.reeco.core.dmp.core.model.*;
import com.reeco.core.dmp.core.repo.*;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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


            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = file.getBytes();

            ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream ));
            String line = "";
            List<NumericalTsByOrg> numericalTsByOrgs = new ArrayList<>();
            List<CategoricalTsByOrg> categoricalTsByOrgs = new ArrayList<>();
            List<ParameterDto> parameterDtos =new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] listLine = line.split(",");

                if (listLine[0].equals("timestamp")) {
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
                    LocalDateTime event_time = LocalDateTime.parse(listLine[0], dtf);
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
                                        nkey, parameterDtos.get(i-1).getParameterName(), parameterDtos.get(i-1).getIndicatorName(), stationId, 0L,
                                        Double.parseDouble(listLine[i]), event_time, lat, lon
                                );
    //                    numericalTsByOrgRepository.save(numericalTsByOrg);
    //                    break;
                                numericalTsByOrgs.add(numericalTsByOrg);
                            }else {
                                CategoricalTsByOrg.Key nkey = new CategoricalTsByOrg.Key(
                                        orgId, date, event_time, parameterDtos.get(i - 1).getParameterId(), listLine[i]
                                );
    //
                                CategoricalTsByOrg categoricalTsByOrg = new CategoricalTsByOrg(
                                        nkey, parameterDtos.get(i-1).getParameterName(), parameterDtos.get(i-1).getIndicatorName(), stationId, 0L,
                                         event_time, lat, lon
                                );
    //                    numericalTsByOrgRepository.save(numericalTsByOrg);
    //                    break;
                                categoricalTsByOrgs.add(categoricalTsByOrg);
                            }


                    }
                }
            }
//            numericalTsByOrgRepository.saveAll(numericalTsByOrgs);
//            categoricalTsByOrgRepository.saveAll(categoricalTsByOrgs);
            br.close();
            TimeUnit.SECONDS.sleep(5);
            return new ResponseMessage("Import data successful!");


    }
}
