package com.reeco.transport.infrastructure.model.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class SFTPMessage extends ProtocolMessage {

    @JsonProperty("hostName")
    String hostName;

    @JsonProperty("port")
    String port;

    @JsonProperty("name")
    String name;

    @JsonProperty("password")
    String password;

    @JsonProperty("useSSL")
    boolean useSSL;

    @JsonProperty("key")
    String key;

    @JsonProperty("ipWhiteList")
    List<String> ipWhiteList;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(List<String> ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SFTPMessage{" +
                "id='" + id + '\'' +
                ", fileType='" + fileType + '\'' +
                ", templateFormat=" + templateFormat +
                ", maximumTimeout=" + maximumTimeout +
                ", maximumAttachment=" + maximumAttachment +
                ", notificationType='" + notificationType + '\'' +
                ", hostName='" + hostName + '\'' +
                ", port='" + port + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", useSSL=" + useSSL +
                ", key='" + key + '\'' +
                ", ipWhiteList=" + ipWhiteList +
                '}';
    }
}
