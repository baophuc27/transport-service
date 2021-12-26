package com.reeco.common.model.dto;

import com.reeco.common.model.enumtype.Protocol;

public interface Connection {
    Protocol getProtocol();

    Long getOrganizationId();

    Long getId();

    void setAccessToken(String accessToken);

    String getAccessToken();
}
