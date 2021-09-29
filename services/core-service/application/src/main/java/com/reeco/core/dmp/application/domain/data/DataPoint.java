package com.reeco.core.dmp.application.domain.data;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class DataPoint {
    private  OffsetDateTime timestamp ;
    private  Float latitude;
    private  Float longitude;
    private  Value value;
}
