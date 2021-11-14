package com.reeco.model;

import lombok.Getter;

import javax.validation.constraints.NotNull;


public abstract class BaseEntity implements Entity {
    @Getter
    @NotNull(message = "id must be note NULL")
    private Long id = -1L;

    public BaseEntity() {
    }

    protected BaseEntity(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
