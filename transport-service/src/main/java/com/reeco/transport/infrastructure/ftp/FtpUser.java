//package com.reeco.transport.infrastructure.ftp;
//
//import lombok.Value;
//import org.apache.ftpserver.ftplet.Authority;
//import org.apache.ftpserver.ftplet.AuthorizationRequest;
//import org.apache.ftpserver.ftplet.User;
//import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
//import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
//import org.apache.ftpserver.usermanager.impl.WritePermission;
//
//import java.io.File;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Value
//public class FtpUser implements User {
//
//    String name;
//
//    String password;
//
//    boolean enabled;
//
//    int maxIdleTime;
//
//    List<Authority> authorities = List.of(
//            new ConcurrentLoginPermission(20, 10),
//            new TransferRatePermission(4800, 4800),
//            new WritePermission());
//
//    File homeDirectory;
//
//    FtpUser(String name, String password, boolean enabled, int maxIdleTime, File homeDirectory) {
//        this.name = name;
//        this.maxIdleTime = maxIdleTime == -1 ?
//                60 : maxIdleTime;
//        this.homeDirectory = homeDirectory;
//        this.password = password;
//        this.enabled = enabled;
//    }
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public List<Authority> getAuthorities() {
//        return this.authorities;
//    }
//
//    @Override
//    public List<Authority> getAuthorities(Class<? extends Authority> aClass) {
//        return this.authorities.stream().filter(
//                a -> a.getClass().isAssignableFrom(aClass))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
//        return this.getAuthorities()
//                .stream()
//                .filter(a -> a.canAuthorize(authorizationRequest))
//                .map(a -> a.authorize(authorizationRequest))
//                .filter(Objects::nonNull)
//                .findFirst()
//                .orElse(null);
//    }
//
//    @Override
//    public int getMaxIdleTime() {
//        return this.maxIdleTime;
//    }
//
//    @Override
//    public boolean getEnabled() {
//        return this.enabled;
//    }
//
//    @Override
//    public String getHomeDirectory() {
//        return this.homeDirectory.getAbsolutePath();
//    }
//}
