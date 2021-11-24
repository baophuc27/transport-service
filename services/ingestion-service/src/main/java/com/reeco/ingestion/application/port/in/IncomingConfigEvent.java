package com.reeco.ingestion.application.port.in;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomingConfigEvent {
     Long orgId;

     Long connectionId;

     String actionType;

     String entityType;

     Parameter parameter;
}
