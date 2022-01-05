package com.reeco.core.dmp.core.service;

import com.reeco.core.dmp.core.dto.SharingRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class DataSharingService {
    public String generateCSVTemplate(SharingRequestDTO request) {
        String[] csvHeader = {
                request.getRequestTitle()
        };

        List<List<String>> csvBody = new ArrayList<>();

        csvBody.add(Arrays.asList("Request ID", "User ID", "User Name"));

        String requestId =String.valueOf(request.getRequestId());
        String userId = String.valueOf(request.getUserId());
        String requestTitle = request.getUserName();

        csvBody.add(Arrays.asList(requestId,userId,requestTitle));

//      Append a record : timestamp, indicator_1, indicator_2,..., lat, lon
        List<String> indicatorRecord = request.getIndicatorList().stream().map(e -> e.getName()).collect(Collectors.toList());
        indicatorRecord.add(0,"timestamp");
        indicatorRecord.add("lat");
        indicatorRecord.add("lon");
        csvBody.add(indicatorRecord);

        ByteArrayInputStream byteArrayOutputStream = null;
        String encodedString = "";

        try{
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
        }
        catch (IOException exception){
            log.info("Exception when create CSV template: {}",exception.getMessage());
        }

        return encodedString;

    }


    public String receiveData(MultipartFile file, Long requestId, Long userId) throws IOException {
        Path root = Paths.get("uploads");
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String fileName = String.format("user_%d_request_%d_%s.csv",userId,requestId,timeStamp);
        Files.copy(
                file.getInputStream(), root.resolve(fileName)
        );
        return fileName;
    }
}
