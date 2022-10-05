package com.reeco.transport.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.*;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.ParamType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.utils.AES;
import com.reeco.transport.application.usecase.DataManagementUseCase;
import com.reeco.transport.domain.*;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import com.reeco.transport.utils.annotators.Infrastructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Infrastructure
@RequiredArgsConstructor
@Slf4j
@Service
public class DataSharingService {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, byte[]> producerTemplate;

    private final DataManagementUseCase dataManagementUseCase;

    private final PostgresDeviceRepository postgresDeviceRepository;

    public String generateCSVTemplate(SharingRequestDTO request) {
        String[] csvHeader = {
                request.getRequestTitle()
        };

        List<List<String>> csvBody = new ArrayList<>();

        csvBody.add(Arrays.asList("Request ID", "User ID", "User Name"));

        String requestId =String.valueOf(request.getRequestId());
        String userId = String.valueOf(request.getUserId());
        String userName = request.getUserName();

        csvBody.add(Arrays.asList(requestId,userId,userName));

//      Append a record : timestamp, indicator_1, indicator_2,..., lat, lon
        List<String> indicatorRecord = request.getIndicatorList().stream().map(DataSharingParamDto::getName).collect(Collectors.toList());
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
        if (Files.notExists(root)) Files.createDirectories(root);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String fileName = String.format("user_%d_request_%d_%s.csv",userId,requestId,timeStamp);
        Files.copy(file.getInputStream(), root.resolve(fileName));
        return fileName;
    }

    public void acceptData(DetailSharingDataDTO detailSharingDataDTO) {
        sendHTTPConnectionMessage(detailSharingDataDTO);
        sendConfigEventMessage(detailSharingDataDTO);

        String regex = String.format("user_%s_request_%s_(.*).csv", detailSharingDataDTO.getUserId(), detailSharingDataDTO.getRequestId());
        String fileName = "";
        File[] directoryListing = new File("./uploads").listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().matches(regex)) {
                    fileName = child.getName();
                }
            }
        } else {
            log.info("No directory!");
        }
        Integer deviceId = Math.toIntExact(detailSharingDataDTO.getConnectionId());

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        String finalFileName = fileName;
        exec.schedule(() -> readSharingFile(finalFileName, deviceId), 10, TimeUnit.SECONDS);
    }

    private void readSharingFile(String fileName, Integer deviceId) {
        File file = new File("./uploads/" + fileName);
        file.setReadable(true);
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            String[] columnName = null;
            int lineIndex = 0;
            while (scanner.hasNext()) {
                switch (lineIndex) {
                    case 0:
                        String requestTitle = scanner.next();
                        break;
                    case 1:
                        // Unused headers
                        scanner.next();
                        break;
                    case 2:
                        String[] request = scanner.next().split(",");
                        String requestId = String.valueOf(request[0]);
                        String userId = String.valueOf(request[1]);
                        String userName = request[2];
                        break;
                    case 3:
                        columnName = scanner.next().split(",");
                        break;
                    default:
                        String[] record = scanner.next().split(",");
                        LocalDateTime timeStamp = getTimeStamp(record[0]);
                        Double lat = Double.valueOf(record[record.length - 2]);
                        Double lon = Double.valueOf(record[record.length - 1]);

                        for (int i=1; i < columnName.length-2; i++) {
                            String keyName = columnName[i];
                            Double value = Double.valueOf(record[i]);
                            LocalDateTime receivedAt = LocalDateTime.now().withNano(0);
                            DataRecord dataRecord = new DataRecord(timeStamp,keyName,value,deviceId,receivedAt,lat,lon);
                            dataManagementUseCase.receiveData(dataRecord);
                        }
                        break;
                }
                lineIndex++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendConfigEventMessage(DetailSharingDataDTO detailSharingDataDTO) {
        Parameter parameter = new Parameter();
        parameter.setConnectionId(detailSharingDataDTO.getConnectionId());
        parameter.setWorkspaceId(detailSharingDataDTO.getWorkspaceId());
        parameter.setOrganizationId(detailSharingDataDTO.getOrganizationId());
        parameter.setStationId(detailSharingDataDTO.getStationId());
        parameter.setDisplayType(detailSharingDataDTO.getDisplayType());
        parameter.setFormat(detailSharingDataDTO.getFormat());
        parameter.setAlarms(Collections.emptyList());
        for (DetailSharingDataParamDTO param : detailSharingDataDTO.getParams()) {
            parameter.setId(param.getParamId());
            parameter.setVietnameseName(param.getVietnameseName());
            parameter.setEnglishName(param.getEnglishName());
            parameter.setParameterType(ParamType.DATA_SHARING);
            parameter.setIndicatorId(param.getIndicatorId());
            parameter.setUnit(param.getUnit());
            parameter.setKeyName(param.getEnglishName() + "_" + detailSharingDataDTO.getConnectionId());
            parameter.setSourceParamName(param.getSourceName());


            ProducerRecord<String, byte[]> msg = null;
            try {
                String TOPIC_NAME = "reeco_config_event";
                msg = new ProducerRecord<>(TOPIC_NAME, objectMapper.writeValueAsString(parameter).getBytes());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            msg.headers()
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.PARAM.name().getBytes());
            producerTemplate.send(msg);
        }
    }

    private void sendHTTPConnectionMessage(DetailSharingDataDTO detailSharingDataDTO) {
        DataSharingConnection connection = new DataSharingConnection();
        connection.setId(detailSharingDataDTO.getConnectionId());
        connection.setOrganizationId(detailSharingDataDTO.getOrganizationId());
        connection.setStationId(detailSharingDataDTO.getStationId());
        connection.setWorkspaceId(detailSharingDataDTO.getWorkspaceId());
        connection.setVietnameseName("vietnamese_name");
        connection.setEnglishName("english_name");
        connection.setActive(Boolean.TRUE);
        connection.setProtocol(Protocol.DATA_SHARING);

        ProducerRecord<String, byte[]> msg = null;
        try {
            String TOPIC_NAME = "reeco_config_event";
            msg = new ProducerRecord<>(TOPIC_NAME, objectMapper.writeValueAsString(connection).getBytes());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        msg.headers()
                .add("actionType", ActionType.UPSERT.name().getBytes())
                .add("entityType", EntityType.CONNECTION.name().getBytes())
                .add("protocol", Protocol.DATA_SHARING.name().getBytes());
        producerTemplate.send(msg);
    }

    private LocalDateTime getTimeStamp(String timeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(timeString,formatter);
    }
}
