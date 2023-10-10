package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.dto.AdminEventParamsDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.UpdateEventAdminRequest;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@Valid @ModelAttribute AdminEventParamsDto eventParamsDto) {
        log.debug("Получен Get-запрос к эндпоинту: /admin/events на получение списка всех событий");

        return new ResponseEntity<>(eventService.getEventsByAdmin(eventParamsDto), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Получен Patch-запрос к эндпоинту: /admin/events/{} на изменение события с id = {}", eventId, eventId);

        return new ResponseEntity<>(eventService.updateByAdmin(eventId, updateEventAdminRequest), HttpStatus.OK);
    }
}
