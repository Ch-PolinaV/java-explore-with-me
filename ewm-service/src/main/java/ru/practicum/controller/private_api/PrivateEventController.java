package ru.practicum.controller.private_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("Получен POST-запрос к эндпоинту: /users/{}/events на сохранение нового события", userId);

        return new ResponseEntity<>(eventService.create(userId, newEventDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.debug("Получен Patch-запрос к эндпоинту: /users/{}/events/{} на изменение события добавленного текущим пользователем", eventId, eventId);

        return new ResponseEntity<>(eventService.update(userId, eventId, updateEventRequest), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusRequest(@PathVariable Long userId,
                                                                             @PathVariable Long eventId,
                                                                             @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("Получен Patch-запрос к эндпоинту: /users/{}/events/{}/requests на изменение статуса заявок на участие в событии текущего пользователя", eventId, eventId);

        return new ResponseEntity<>(requestService.updateStatusRequest(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllByInitiatorId(@PathVariable Long userId,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен Get-запрос к эндпоинту: /users/{}/events на получение списка всех событий добавленных текущим пользователем", userId);

        return new ResponseEntity<>(eventService.getAllByInitiatorId(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Получен Get-запрос к эндпоинту: /users/{}/events/{} на получение полной информации о событии добавленном текущим пользователем", userId, eventId);

        return new ResponseEntity<>(eventService.getEventInfoByInitiatorId(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Получен Get-запрос к эндпоинту: /users/{}/events/{}/requests на получение информации о запросах на участие в событии текущего пользователя", userId, eventId);

        return new ResponseEntity<>(requestService.getEventRequests(userId, eventId), HttpStatus.OK);
    }
}