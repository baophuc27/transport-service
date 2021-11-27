package com.reeco.transport.infrastructure;

import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.utils.annotators.Infrastructure;
import com.reeco.transport.utils.exception.FileProcessingException;
import com.reeco.transport.application.usecase.DataManagementUseCase;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Infrastructure
@RequiredArgsConstructor
@Slf4j
public class FileProcessor {

    private WatchService watchService;

    private final DataManagementUseCase dataManagementUseCase;

    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Value(value = "${application.file-directory}")
    private String FILE_DIRECTORY;

    @PostConstruct
    private void postProcess(){
        try{
            watchService = FileSystems.getDefault().newWatchService();
        }
        catch (IOException ex){
            log.warn("Got an exception when creating file watcher for SFTP files: {}",ex.getMessage());
            throw new FileProcessingException(ex);
        }
    }

    public void observe(String subFolder) {

        String relativePath = FILE_DIRECTORY + subFolder;
        Path path = Paths.get(relativePath);
        try{
            log.info("Begin observing SFTP files for device: {}",subFolder);
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        }
        catch (IOException ex){
            log.warn("Got an exception when register file watcher for: {}",subFolder);
            throw new FileProcessingException(ex.getMessage());
        }
    }

    public void createDirectory(String subFolder){
        String relativePath = FILE_DIRECTORY + subFolder;
        File directoryCreator = new File(relativePath);
        if (directoryCreator.exists()){
            log.info("Folder {} is existed",relativePath);
        }
        directoryCreator.mkdir();
        log.info("Created new folder: {}",relativePath);
    }



    @EventListener(ApplicationReadyEvent.class)
    private void watchFile() throws InterruptedException {
        WatchKey watchKey;

        while ((watchKey = watchService.take()) != null){
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                log.info("[FILE PROCESSOR] - {} : {}",event.kind(),event.context());
                    if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)){
                        Path deviceDir = (Path) watchKey.watchable();
                        readFile(event.context().toString(),deviceDir);
                    }
                }
            watchKey.reset();
        }
    }

    private void readFile(String fileName,Path deviceDir){
        String filePath = deviceDir +"/" +fileName;
        String folderName = deviceDir.getFileName().toString();
        int stationId = Integer.parseInt(folderName);
        int templateId = postgresDeviceRepository.findTemplateById(stationId);
        log.warn("Device info: {}",postgresDeviceRepository.findDeviceById(stationId));
        switch (templateId){
            case 1:
                readFile1(filePath,stationId);
                break;
            case 2:
                readFile2(filePath,stationId);
                break;
            default:
                log.warn("Invalid template");
        }
    }

    private void readFile2(String filePath, int stationId) {
            try{
                File file = new File(filePath);
                file.setReadable(true);
                Scanner scanner = new Scanner(file);
                LocalDateTime timeStamp = LocalDateTime.MIN;

                scanner.useDelimiter(",");
                Thread.sleep(2000);
                while(scanner.hasNext()){
                    String record = scanner.next();
                    String[] splitRecord = record.split(":");
                    if (splitRecord.length == 1){
                        timeStamp = getTimeStamp(splitRecord[0]);
                    }
                    if (splitRecord.length == 2){
                        String key = splitRecord[0];
                        String[] valueRecord = splitRecord[1].split(" ");
                        if (valueRecord.length == 2){
                            Double value = Double.valueOf(valueRecord[0]);
                            DataRecord dataRecord = new DataRecord(0,timeStamp,key,value,stationId,LocalDateTime.now().withNano(0),10.0,103.0);
                            log.info("Template 2 record: {}",dataRecord.toString());
                            dataManagementUseCase.receiveData(dataRecord);
                        }
                    }
                }
                scanner.close();
            }
            catch (InterruptedException | RuntimeException | IOException exception){
                log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
            }
    }

    private void readFile1(String filePath, int stationId) {
        try{
            File file = new File(filePath);
            file.setExecutable(true);
            file.setReadable(true);
            Thread.sleep(2000);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            while(scanner.hasNext()){
                String record = scanner.next();
                String[] splitRecord = record.split("\t");
                LocalDateTime timestamp = getTimeStamp(splitRecord[3]);
                String key = splitRecord[0];
                Double value = Double.valueOf(splitRecord[1]);
                DataRecord dataRecord = new DataRecord(0,timestamp,key,value,stationId,LocalDateTime.now().withNano(0),null,null);
                log.info("Template 2 record: {}",dataRecord.toString());
                dataManagementUseCase.receiveData(dataRecord);
            }
            scanner.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
                log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
            }
    }

    private void readFileO(String fileName,Path deviceDir) {
        String filePath = deviceDir +"/" +fileName;
        Path path = Paths.get(filePath);
        try{
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println(Files.readAllLines(path));
                } catch (IOException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            }).start();
            List<String> read = Files.readAllLines(path);

            System.out.println("hello string"+read);
        }
        catch (IOException exception){
            log.warn("Got an exception when reading file: {}", fileName);
            throw new FileProcessingException(exception.getMessage());
        }
    }
    private LocalDateTime getTimeStamp(String timeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(timeString,formatter);
    }
}
