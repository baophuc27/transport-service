package com.reeco.model.dto;

import com.reeco.model.BaseEntity;
import com.reeco.model.Connection;
import com.reeco.model.Protocol;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
public class BaseConnectionDTO extends BaseEntity implements Connection {

    private static final long serialVersionUID = 1L;

    private String englishName;

    private String vietnameseName;

    private Protocol protocol;

    public BaseConnectionDTO(Long id) {
        super(id);
    }

    public BaseConnectionDTO(Long id, String englishName, String vietnameseName, Protocol protocol) {
        super(id);
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.protocol = protocol;
    }
}
