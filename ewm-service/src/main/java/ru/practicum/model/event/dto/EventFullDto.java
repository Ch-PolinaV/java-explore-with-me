package ru.practicum.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.enums.State;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}