package com.reeco.core.dmp.core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tinkerpop.shaded.jackson.annotation.JsonInclude;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionHistory {

    private Long organizationId;

    private Long connectionId;

    private Integer historyId;

    private Long alarmTime;

    private String alarmType;

    private String protocol;

    private String description;



}
