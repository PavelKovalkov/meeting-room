package com.globant.demo.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MeetingRoomLocalDateParser implements LocalDateParser {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HH:mm:ss");

    @Override
    public Long toEpochMillis(String localDate) {
        return LocalDateTime.parse(localDate, DATE_TIME_FORMATTER).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
