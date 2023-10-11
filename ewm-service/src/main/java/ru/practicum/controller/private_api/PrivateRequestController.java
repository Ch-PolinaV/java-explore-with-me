package ru.practicum.controller.private_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getByRequester(@PathVariable Long userId) {
        log.debug("Получен Get-запрос к эндпоинту: /users/{}/requests на получение информации о заявках текущего пользователя на участие в чужих событиях", userId);

        return new ResponseEntity<>(requestService.getByRequester(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createEventRequest(@PathVariable Long userId,
                                                                      @RequestParam Long eventId) {
        log.debug("Получен POST-запрос к эндпоинту: /users/{}/requests на сохранение запроса от текущего пользователя на участие в событии", userId);

        return new ResponseEntity<>(requestService.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelEventRequest(@PathVariable Long userId,
                                                                      @PathVariable Long requestId) {
        log.debug("Получен Patch-запрос к эндпоинту: /users/{}/requests/{}/cancel на отмену своего запроса на участие в событии", userId, requestId);

        return new ResponseEntity<>(requestService.cancel(userId, requestId), HttpStatus.OK);
    }
}
