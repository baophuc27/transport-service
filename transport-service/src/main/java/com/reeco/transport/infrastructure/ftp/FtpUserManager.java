//package com.reeco.transport.infrastructure.ftp;
//
//import com.reeco.transport.application.repository.FtpRepository;
//import com.reeco.transport.domain.protocol.FTPConfiguration;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ftpserver.ftplet.*;
//import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
//import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
//import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
//import org.apache.ftpserver.usermanager.impl.WritePermission;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.util.Assert;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//public class FtpUserManager implements UserManager {
//
//    private final FtpRepository ftpRepository;
//
//    @Value(value = "${application.file-directory}")
//    private String FILE_DIRECTORY;
//
//    @Override
//    public User getUserByName(String s) throws FtpException {
//        FTPConfiguration ftpConfiguration =  ftpRepository.findByUserName(s);
//        File home = new File(FILE_DIRECTORY, ftpConfiguration.getFolderName());
//        int maxIdleTime = ftpConfiguration.getMaximumTimeoutSeconds()*60;
//        return new FtpUser(ftpConfiguration.getUserName(),ftpConfiguration.getPassword(),true,maxIdleTime, home);
//    }
//
//    @Override
//    public String[] getAllUserNames() throws FtpException {
//        return ftpRepository.getAllUserName();
//    }
//
//    @Override
//    public void delete(String s) throws FtpException {
//        log.info("DELETED");
//    }
//
//    @Override
//    public void save(User user) throws FtpException {
//        log.info("SAVED");
//    }
//
//    @Override
//    public boolean doesExist(String username) throws FtpException {
//        return getUserByName(username) != null;
//    }
//
//    @SneakyThrows
//    @Override
//    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
//        UsernamePasswordAuthentication upw = (UsernamePasswordAuthentication) authentication;
//        String user = upw.getUsername();
//        return Optional
//                .ofNullable(this.getUserByName(user))
//                .filter(u -> {
//                    String incomingPw = u.getPassword();
//                    return encode(incomingPw).equalsIgnoreCase(u.getPassword());
//                })
//                .orElseThrow(() -> new AuthenticationFailedException("Authentication has failed! Try your username and password."));
//    }
//
//    private String encode(String pw) {
//        return pw;
//    }
//
//    @Override
//    public String getAdminName() throws FtpException {
//        return "admin";
//    }
//
//    @Override
//    public boolean isAdmin(String s) throws FtpException {
//        return getAdminName().equalsIgnoreCase(s);
//    }
//}
