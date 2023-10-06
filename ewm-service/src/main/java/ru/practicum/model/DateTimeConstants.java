package ru.practicum.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConstants {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final LocalDateTime MAX_TIME = LocalDateTime.parse("3000-01-01 00:00:00", FORMATTER);
    public static final LocalDateTime MIN_TIME = LocalDateTime.parse("2000-01-01 00:00:00", FORMATTER);
}