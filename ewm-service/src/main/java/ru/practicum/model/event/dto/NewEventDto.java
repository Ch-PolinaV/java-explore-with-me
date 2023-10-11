package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.location.dto.LocationDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.model.DateTimeConstants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @Size(max = 2000, min = 20)
    @NotNull
    private String annotation;
    @NotNull
    private Long category;
    @Size(max = 7000, min = 20)
    @NotNull
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @Size(max = 120, min = 3)
    @NotNull
    private String title;
}