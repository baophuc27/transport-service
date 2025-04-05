package com.reeco.config.controller.base;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;

@RequiredArgsConstructor
@CrossOrigin(value = {"*"})
public abstract class BaseController {
    protected final String TOPIC_NAME = "reeco_config_event";
}