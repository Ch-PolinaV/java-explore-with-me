package ru.practicum.service.event;

import ru.practicum.model.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventRequest updateEvent);

    EventFullDto updateByAdmin(Long eventId, UpdateEventRequest updateEvent);

    List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size);

   EventFullDto getEventInfoByInitiatorId(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(AdminEventParamsDto eventParamsDto);

    EventFullDto getPublicEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getPublicEvents(PublicEventParamsDto eventParamsDto, HttpServletRequest request);
}