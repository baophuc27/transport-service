package com.reeco.core.dmp.application.domain.governance;

import com.reeco.core.dmp.application.domain.data.Category;
import com.reeco.core.dmp.application.domain.data.DataSource;
import com.reeco.core.dmp.application.domain.data.Tag;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class View
{
    private String id;

    private int viewId;

    private String name;

    private ViewType viewType;

    private String userId;


    Set<DataSource> dataSources = new HashSet<>();

    Set<Category> categories = new HashSet<>();

    Set<Tag> tags = new HashSet<>();





}
