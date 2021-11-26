package com.reeco.ingestion.application.port.in;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
//@Getter
//@Builder
@NoArgsConstructor
public class IncomingConfigEvent {
     @JsonProperty("orgId")
     Long orgId;

     @JsonProperty("connectionId")
     Long connectionId;

     @JsonProperty("actionType")
     String actionType;

     @JsonProperty("entityType")
     String entityType;

     @JsonProperty("parameter")
     Parameter parameter;

     @JsonProperty("createdTime")
     Long createdTime;
}
