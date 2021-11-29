package com.reeco.ingestion.domain;

import com.reeco.common.model.dto.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.kafka.common.message.LeaderAndIsrRequestData;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ParamAndAlarm {

    private Long paramId;

    private List<Alarm> alarms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamAndAlarm)) return false;
        ParamAndAlarm that = (ParamAndAlarm) o;
        return Objects.equals(getParamId(), that.getParamId()) &&
                Objects.equals(getAlarms(), that.getAlarms());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParamId(), getAlarms());
    }
}
