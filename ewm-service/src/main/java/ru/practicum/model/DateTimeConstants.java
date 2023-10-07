package ru.practicum.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConstants {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String START = LocalDateTime.now().format(FORMATTER);
    public static final String END = LocalDateTime.now().plusYears(100).format(FORMATTER);
}