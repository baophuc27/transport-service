package com.reeco.ingestion.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utils {
    public static LocalDate convertDateTimeToDate(LocalDateTime dateTime){
        return dateTime.toLocalDate();
    }
}
