package ru.practicum.controller.public_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.PublicEventParamsDto;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@Valid @ModelAttribute PublicEventParamsDto eventParamsDto, HttpServletRequest request) {
        log.debug("Получен Get-запрос к эндпоинту: /events на получение событий с возможностью фильтрации");

        return new ResponseEntity<>(eventService.getPublicEvents(eventParamsDto, request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id,
                                                HttpServletRequest request) {
        log.debug("Получен Get-запрос к эндпоинту: /events/{} на получение подробной информации об опубликованном событии по его идентификатору", id);

        return new ResponseEntity<>(eventService.getPublicEvent(id, request), HttpStatus.OK);
    }
}