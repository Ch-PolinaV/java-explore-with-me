package ru.practicum.model.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.Status;

import java.time.LocalDateTime;

import static ru.practicum.model.DateTimeConstants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private Status status;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;
}
