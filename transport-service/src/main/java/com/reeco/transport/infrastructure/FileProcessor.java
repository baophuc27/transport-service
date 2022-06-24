package com.reeco.transport.infrastructure;

import com.reeco.transport.application.usecase.AlarmManagementUsecase;
import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
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
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.regex.*;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Infrastructure
@RequiredArgsConstructor
@Slf4j
public class FileProcessor {

    private WatchService watchService;

    private final DataManagementUseCase dataManagementUseCase;

    private final AlarmManagementUsecase alarmManagementUsecase;

    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Value(value = "${application.file-directory}")
    private String FILE_DIRECTORY;

    @Value(value = "${application.alarm-logs-directory}")
    private String ALARM_LOGS_DIRECTORY;


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

    @PostConstruct
    private void watchAlarmLogs(){
        String relativePath = ALARM_LOGS_DIRECTORY;
        createDirectory(relativePath);
        observe(relativePath);
    }

    @PostConstruct
    private void watchRegisteredFolder(){
        List<Integer> registered_devices = postgresDeviceRepository.getRegisteredDevices();
        for (Integer device_id : registered_devices){
            createDirectory(FILE_DIRECTORY + device_id);
            observe(FILE_DIRECTORY + device_id);
        }
    }

    public void observe(String relativePath) {
        Path path = Paths.get(relativePath);
        try{
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
            log.info("Observing files in: {}",relativePath);
        }
        catch (IOException ex){
            log.warn("Got an exception when register file watcher for: {}",relativePath);
            throw new FileProcessingException(ex.getMessage());
        }
    }

    public void createDirectory(String relativePath){
//        String relativePath = FILE_DIRECTORY + subFolder;
        File directoryCreator = new File(relativePath);
        log.info(relativePath);
        if (directoryCreator.exists()){
            log.info("Folder {} is existed",relativePath);
        } else {
            directoryCreator.mkdirs();
            log.info("Created new folder: {}", relativePath);
        }
    }



    @EventListener(ApplicationReadyEvent.class)
    private void watchFile() throws InterruptedException {
        WatchKey watchKey;

        while ((watchKey = watchService.take()) != null){
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                log.info("[FILE PROCESSOR] - {} : {}",event.kind(),event.context());
                    if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)){
                        Path watchedDir = (Path) watchKey.watchable();
                        log.info(String.valueOf(watchedDir));
                        String fileName = event.context().toString();
                        if (String.valueOf(watchedDir).equals(ALARM_LOGS_DIRECTORY)){
                            readLogsFile(fileName);
                        } else {
                            readDataFile(fileName, watchedDir);
                        }
                    }
                }
            watchKey.reset();
        }
    }
    private String getNowTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
    private void readLogsFile(String filename){
        String cleanedFileName = filename.replace("~","");
        log.info("Getting logs: {}",cleanedFileName);
        String filePath = ALARM_LOGS_DIRECTORY + "/" + cleanedFileName;
        switch (cleanedFileName){
            case "auth_failed.txt":
                authFailedAlarm(filePath);
                break;
            case "logout.txt":
                logoutAlarm(filePath);
                break;
            case "connect.txt":
                connectAlarm(filePath);
                break;
            default:
                break;
        }
    }

    private void authFailedAlarm(String filePath){
        log.info("auth failed ALARM");
        String regex = "\\[(.*?)\\]";
        Pattern p = Pattern.compile(regex);
        try{
            File file = new File(filePath);
            file.setReadable(true);
            Scanner scanner = new Scanner(file);

            scanner.useDelimiter("\n");

            Thread.sleep(2000);
            while(scanner.hasNext()){
                String record = scanner.next();
                log.info(record);
                Matcher m = p.matcher(record);
                while (m.find()){
                    String userName = m.group(1);
                    if  (!userName.equals("WARNING")){
                        alarmManagementUsecase.alarmAuthenticationFailed(userName,getNowTime());
                    }
                }
            }
            scanner.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
            log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
        }
    }

    private void logoutAlarm(String filePath){
        log.info("Logout ALARM");
        String regex = "\\((.*?)@";
        Pattern p = Pattern.compile(regex);
        try{
            File file = new File(filePath);
            file.setReadable(true);
            Scanner scanner = new Scanner(file);

            scanner.useDelimiter("\n");

            Thread.sleep(2000);
            while(scanner.hasNext()){
                String record = scanner.next();
                log.info(record);
                Matcher m = p.matcher(record);
                while (m.find()){
                    String userName = m.group(1);
                    if  (!userName.equals("WARNING")){
                        alarmManagementUsecase.alarmDisconnected(userName,getNowTime());
                    }
                }
            }
            scanner.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
            log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
        }
    }

    private void connectAlarm(String filePath){
        log.info("Connected ALARM");
        String regex = "\\[INFO\\] (.*?) is now logged in";
        Pattern p = Pattern.compile(regex);
        try{
            File file = new File(filePath);
            file.setReadable(true);
            Scanner scanner = new Scanner(file);

            scanner.useDelimiter("\n");

            Thread.sleep(2000);
            while(scanner.hasNext()){
                String record = scanner.next();
                log.info(record);
                Matcher m = p.matcher(record);
                while (m.find()){
                    String userName = m.group(1);
                    alarmManagementUsecase.alarmConnected(userName,getNowTime());
                }
            }
            scanner.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
            log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
        }
    }

    private void readDataFile(String fileName, Path deviceDir){
        String filePath = deviceDir + "/" + fileName;
        String folderName = deviceDir.getFileName().toString();
        int deviceId = Integer.parseInt(folderName);
        int templateId = postgresDeviceRepository.findTemplateById(deviceId);
        postgresDeviceRepository.updateDeviceActive(deviceId);
        DeviceEntity device = postgresDeviceRepository.findDeviceById(deviceId);
        if (device.getActive()){
            switch (templateId){
                case 1:
                    readFile1(filePath,deviceId);
                    break;
                case 2:
                    readFile2(filePath,deviceId);
                    break;
                case 3:
                    readFile3(filePath,deviceId);
                    break;
                default:
                    log.warn("Invalid template");
                    break;
            }
        }
    }

    private void readFile2(String filePath, int deviceId) {
        try{
            File file = new File(filePath);
            file.setReadable(true);
            Scanner scanner = new Scanner(file);
            LocalDateTime timeStamp = LocalDateTime.MIN;

            scanner.useDelimiter(",");
            Thread.sleep(2000);
            Double lat = 0.;
            Double lon = 0.;
            while(scanner.hasNext()) {
                String record = scanner.next();
                String[] splitRecord = record.split(":");
                if (splitRecord.length == 2) {
                    String key = splitRecord[0];
                    if (key.strip().equals("Lat")) {
                        lat = Double.valueOf(splitRecord[1]);
                    }
                    if (key.strip().equals("Long")) {
                        lon = Double.valueOf(splitRecord[1]);
                    }
                }
            }
            scanner.close();

            Scanner scanner2 = new Scanner(file);
            scanner2.useDelimiter(",");
            Thread.sleep(2000);

            while(scanner2.hasNext()){
                String record = scanner2.next();
                String[] splitRecord = record.split(":");
                if (splitRecord.length == 1){
                    timeStamp = getTimeStamp(splitRecord[0]);
                }
                if (splitRecord.length == 2){
                    String key = splitRecord[0];
                    String[] valueRecord = splitRecord[1].split(" ");
                    if (valueRecord.length == 2){
                        Double value = Double.valueOf(valueRecord[0]);
                        DataRecord dataRecord = new DataRecord(timeStamp,key,value,deviceId,LocalDateTime.now().withNano(0),lat,lon);
                        log.info("Template 2 record: {}",dataRecord.toString());
                        dataManagementUseCase.receiveData(dataRecord);
                    }
                }
            }
            scanner2.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
            log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
        }
    }

    private void readFile1(String filePath, int deviceId) {
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
                DataRecord dataRecord = new DataRecord(timestamp,key,value,deviceId,LocalDateTime.now().withNano(0),null,null);
                log.info("Template 1 record: {}",dataRecord.toString());
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

    private void readFile3(String filePath, int deviceId) {
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
                DataRecord dataRecord = new DataRecord(timestamp,key,value,deviceId,LocalDateTime.now().withNano(0),null,null);
                log.info("Template 3 record: {}",dataRecord.toString());
                dataManagementUseCase.receiveData(dataRecord);
            }
            scanner.close();
        }
        catch (InterruptedException | RuntimeException | IOException exception){
            log.warn("Got an exception when reading file {} :{}", filePath,exception.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 */3 * *")
    private void scheduledClean() {
        List<Integer> registered_devices = postgresDeviceRepository.getRegisteredDevices();
        for (Integer device_id : registered_devices) {
            File dataFolder = new File(FILE_DIRECTORY + device_id);
            try {
                if (dataFolder.isDirectory()) {
                    File[] files = dataFolder.listFiles();
                    if (files != null && files.length > 0) {
                        log.info("Start cleaning FTP data folder " + dataFolder);
                        DeviceEntity device = postgresDeviceRepository.findDeviceById(device_id);
                        List<String> deletedFile = new ArrayList<>();

                        long maximumTimeoutDate = System.currentTimeMillis() - device.getMaximumTimeout() * 86400000L;
                        Arrays.stream(files)
                                .filter(file -> (file.lastModified() < maximumTimeoutDate))
                                .forEach(file -> {
                                    if (file.delete()) deletedFile.add(file.getName());
                                });

                        int maximumAttachment = device.getMaximumAttachment();
                        while (files.length > maximumAttachment) {
                            File oldestFile = Arrays.stream(files).min(Comparator.comparing(File::lastModified)).get();
                            if (oldestFile.delete()) deletedFile.add(oldestFile.getName());
                            files = dataFolder.listFiles();
                        }
                        log.info("Deleted total " + deletedFile.size() + " files: " + deletedFile);
                    }
                }
            }
            catch (RuntimeException exception) {
                log.warn("Got an exception when cleaning folder {} :{}", dataFolder, exception.getMessage());
            }
        }
    }

    private LocalDateTime getTimeStamp(String timeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(timeString,formatter);
    }
}
