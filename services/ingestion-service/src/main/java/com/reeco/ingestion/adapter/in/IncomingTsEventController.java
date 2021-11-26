package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.ReecoServiceIngestion;
import com.reeco.ingestion.application.port.in.IncomingAlarm;
import com.reeco.ingestion.application.port.in.IncomingConfigEvent;
import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.Parameter;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomingTsEventController {

    private final StoreTsEventUseCase storeTsEventUseCase;

    private final StoreConfigUseCase storeConfigUseCase;

    private final AlarmCacheUseCase alarmCacheUseCase;




    @KafkaListener(topics = "reeco_time_series_event",containerFactory = "timeSeriesEventListener")
    public void listen(@Headers Map<String,byte[]> header, @Payload IncomingTsEvent message){
        storeTsEventUseCase.storeEvent(message);
    }

    @KafkaListener(topics = "reeco_config_event", containerFactory = "configEventListener")
    public void listen(@Headers Map<String, byte[]> header, @Payload String config) throws JsonProcessingException {



        ObjectMapper mapper = new ObjectMapper();
        IncomingConfigEvent incomingConfigEvent  = mapper.readValue(config, IncomingConfigEvent.class);
//        try {
//
//            JSONObject jsonObject = new JSONObject(config);
//            JSONObject param = jsonObject.getJSONObject("parameter");
//            JSONArray alarm = param.getJSONArray("alarms");
////            List<IncomingAlarm> alarmList = new ArrayList<>();
////            for (int i =0; i< alarm.length();i++){
////                alarmList.add(new IncomingAlarm(alarm.getJSONObject(i).getLong("id"),
////                        alarm.getJSONObject(i).getString("englishName"),
////                        alarm.getJSONObject(i).getString("vietnameseName"),
////                        alarm.getJSONObject(i).getString("alarmType"),
////                        alarm.getJSONObject(i).getString("minValue"),
////                        alarm.getJSONObject(i).getString("maxValue"),
////                        alarm.getJSONObject(i).getString("maintainType"),
////                        alarm.getJSONObject(i).getLong("numOfMatch"),
////                        alarm.getJSONObject(i).getLong("frequence"),
////                        alarm.getJSONObject(i).getString("frequenceType")));
////            }
////            Parameter parameter = new Parameter(
////                    param.getLong("id"), param.getLong("indicatorId"), param.getString("englishName"),
////                    param.getString("parameterType"), alarmList
////            );
////            incomingConfigEvent = new IncomingConfigEvent(jsonObject.getLong("orgId"),jsonObject.getLong("connectionId"),
////                    jsonObject.getString("actionType"), jsonObject.getString("entityType"), parameter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        storeConfigUseCase.storeConfig(incomingConfigEvent);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        alarmCacheUseCase.loadDataToCache();

        System.out.println("hello world, I have just started up");
    }
}
