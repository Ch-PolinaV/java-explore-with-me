package ru.practicum.service.event;

import ru.practicum.model.enums.Sort;
import ru.practicum.model.enums.State;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateEvent);

    Event getEventById(Long eventId);

    List<Event> getAllByIds(List<Long> eventIds);

    List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size);

   EventFullDto getEventInfoByInitiatorId(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto getPublicEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                        Sort sort, Integer from, Integer size, HttpServletRequest request);
}