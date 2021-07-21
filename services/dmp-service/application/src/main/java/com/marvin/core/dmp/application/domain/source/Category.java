package com.marvin.core.dmp.application.domain.source;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Category
{
    private String id;

    private String name;

    private Set<Category> children = new HashSet<>();

    private  Category parent;



}

