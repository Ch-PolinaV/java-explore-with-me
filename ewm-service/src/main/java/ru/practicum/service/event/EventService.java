package ru.practicum.service.event;

import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateEvent);

    Event getEventById(Long eventId);

    Set<Event> getAllByIds(Set<Long> eventIds);

    List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size);

   EventFullDto getEventInfoByInitiatorId(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(AdminEventParamsDto eventParamsDto);

    EventFullDto getPublicEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getPublicEvents(PublicEventParamsDto eventParamsDto, HttpServletRequest request);
}