package com.reeco.model;

import lombok.Getter;

public abstract class BaseEntity implements Entity {
    @Getter
    private Long id = -1L;

    public BaseEntity() {
    }

    protected BaseEntity(Long id) {
        this.id = id;
    }
}
