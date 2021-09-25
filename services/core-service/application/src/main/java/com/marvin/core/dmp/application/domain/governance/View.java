package com.marvin.core.dmp.application.domain.governance;

import com.marvin.core.dmp.application.domain.source.Category;
import com.marvin.core.dmp.application.domain.source.DataSource;
import com.marvin.core.dmp.application.domain.source.Tag;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class View
{
    private String id;

    private int viewId;

    private String name;

    private Type type;

    private String userId;


    Set<DataSource> dataSources = new HashSet<>();

    Set<Category> categories = new HashSet<>();

    Set<Tag> tags = new HashSet<>();



}
