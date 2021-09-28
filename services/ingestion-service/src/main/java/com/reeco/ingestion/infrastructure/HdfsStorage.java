package com.reeco.ingestion.infrastructure;


import com.reeco.ingestion.domain.DeviceConnection;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

@Component
public class HdfsStorage extends MainStorage {

    private final Configuration configuration;

    private FileSystem fileSystem;

    @Value("${hdfs.configuration.server}")
    private String HDFS_SERVER;

    @Value("${hdfs.configuration.user")
    private String HDFS_USER;

    @Value("${hdfs.configuration.rootDirectory}")
    private String ROOT_DIRECTORY;

    public HdfsStorage(){
        this.configuration = new Configuration();
    }

    @PostConstruct
    private void process() throws IOException{
        //FileSystem fileSystem = FileSystem.get(this.configuration);
        //configuration.set("fs.defaultFS",HDFS_SERVER);
    }

    @Override
    public void createDirectory(DeviceConnection device) throws IOException {
//        String directoryName = ROOT_DIRECTORY+"/"+device.getDeviceId();
//        Path path = new Path(directoryName);
//        fileSystem.mkdirs(path);
    }

    @Override
    public void addFile(String directory) {
//        Stream.of(new File(directory).listFiles())
//                .filter(file -> !file.isDirectory())
//                .map(File::getName)
//                .forEach(file ->{
//                    try{
//                        InputStream in = new BufferedInputStream(new FileInputStream(directory+"/"+file));
//                        String hdfsFilePath = ROOT_DIRECTORY + "/" + directory.split("/")[1]+"/"+file;
//                        FileSystem fs = FileSystem.get(URI.create(hdfsFilePath), this.configuration);
//                        OutputStream out = fs.create(new Path(hdfsFilePath));
//                        IOUtils.copyBytes(in,out,512,true);
//                    }
//                    catch (IOException ex){
//                        ex.printStackTrace();
//                    }
//                });
    }

    @Override
    public void readFile(String filepath){

    }

    @Override
    public void deleteFile(String filepath){

    }

}
