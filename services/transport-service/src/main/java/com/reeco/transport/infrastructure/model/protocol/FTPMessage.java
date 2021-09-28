package com.reeco.transport.infrastructure.model.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FTPMessage extends ProtocolMessage {

    @JsonProperty("hostName")
    String hostName;

    @JsonProperty("port")
    String port;

    @JsonProperty("userName")
    String userName;

    @JsonProperty("password")
    String password;

    @JsonProperty("useSSL")
    boolean useSSL;

    @JsonProperty("key")
    String key;

    @JsonProperty("ipWhiteList")
    List<String> ipWhiteList;

    @JsonProperty("removeAfterDays")
    String removeAfterDays;

    @JsonProperty("protocol")
    String protocol;

    @JsonProperty("vietnameseName")
    String vietnameseName;

    @JsonProperty("englishName")
    String englishName;

    public String getVietnameseName() {
        return vietnameseName;
    }

    public void setVietnameseName(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getRemoveAfterDays() {
        return removeAfterDays;
    }

    public void setRemoveAfterDays(String removeAfterDays) {
        this.removeAfterDays = removeAfterDays;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "FTPMessage{" +
                "hostName='" + hostName + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", useSSL=" + useSSL +
                ", key='" + key + '\'' +
                ", ipWhiteList=" + ipWhiteList +
                ", removeAfterDays='" + removeAfterDays + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", id='" + id + '\'' +
                ", fileType='" + fileType + '\'' +
                ", templateFormat=" + templateFormat +
                ", maximumTimeout=" + maximumTimeout +
                ", maximumAttachment=" + maximumAttachment +
                ", notificationType='" + notificationType + '\'' +
                '}';
    }
}
