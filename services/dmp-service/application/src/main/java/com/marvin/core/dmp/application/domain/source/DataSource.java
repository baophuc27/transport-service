package com.marvin.core.dmp.application.domain.source;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class DataSource
{
    private String sourceId;

    private OffsetDateTime createdTime;

    private OffsetDateTime deletedTime;

    private Category category;

    private Set<Tag> tagSet = new HashSet<>();

    private Boolean isActive;

    private String description;



}
