package ru.practicum.controller.private_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentCreateDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.CommentUpdateDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> create(@PathVariable Long userId,
                                             @Valid @RequestBody CommentCreateDto createDto) {
        log.debug("Получен POST-запрос к эндпоинту: /users/{}/comments на сохранение нового комментария", userId);

        return new ResponseEntity<>(commentService.create(userId, createDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<CommentDto> update(@PathVariable Long userId,
                                             @Valid @RequestBody CommentUpdateDto updateDto) {
        log.debug("Получен Patch-запрос к эндпоинту: /users/{}/comments на изменение комментария добавленного текущим пользователем", userId);

        return new ResponseEntity<>(commentService.update(userId, updateDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllByUser(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен Get-запрос к эндпоинту: /users/{}/comments на получение списка всех комментариев оставленных текущим пользователем", userId);

        return new ResponseEntity<>(commentService.getAllByUser(userId, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long commentId) {
        log.debug("Получен Delete-запрос к эндпоинту: /users/{}/comments/{} на удаление комментария с id = {}", userId, commentId, commentId);

        commentService.deleteByUser(userId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
