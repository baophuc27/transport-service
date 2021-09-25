package com.reeco.transport.infrastructure.model;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class DataRecordMessage {
    Integer station_id;

    Integer connection_id;

    String event_ts;

    String received_at;

    String sent_at;

    String date;

    String parameter;

    Double measure;

    Double lat;

    Double lon;

}
