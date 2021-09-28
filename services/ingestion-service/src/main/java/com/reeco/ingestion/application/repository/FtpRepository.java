package com.reeco.ingestion.application.repository;

import com.reeco.ingestion.domain.protocol.FTPConfiguration;
import org.jetbrains.annotations.NotNull;

public interface FtpRepository {

    FTPConfiguration findByUserName(String userName);

    void save(@NotNull FTPConfiguration ftpConfiguration);

    void insert(FTPConfiguration ftpConfiguration);

    void deleteById(String id);

    void deleteByUserName(String userName);

    String[] getAllUserName();
}
