package ru.practicum.model.event.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.enums.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.model.DateTimeConstants.DATE_TIME_FORMAT;

@Data
public class PublicEventParamsDto {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable = false;
    private Sort sort;
    private Integer from = 0;
    private Integer size = 10;
}