package ru.practicum.model.event.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.enums.State;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.model.DateTimeConstants.DATE_TIME_FORMAT;

@Data
public class AdminEventParamsDto {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;
    private Integer from = 0;
    private Integer size = 10;
}
