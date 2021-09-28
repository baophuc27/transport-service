package com.reeco.ingestion.infrastructure.persistence.postgresql;

import javax.persistence.*;

@Entity
@Table(name = "ftp")
public class FtpEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "key")
    private String key;

    @Column(name = "host_name")
    private String hostName;

    @Column(name = "host_port")
    private int hostPort;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "directory")
    private String directory;

    public FtpEntity(int id, String userName, String password, String key, String hostName, int hostPort, String fileType) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.key = key;
        this.hostName = hostName;
        this.hostPort = hostPort;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.setDirectory("/data/"+id);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getHostPort() {
        return hostPort;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public FtpEntity() {
    }

    @Override
    public String toString() {
        return "FtpEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", key='" + key + '\'' +
                ", hostName='" + hostName + '\'' +
                ", hostPort=" + hostPort +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
