package ru.practicum.controller.public_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllByEvent(@RequestParam Long eventId,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен Get-запрос к эндпоинту: /comments/event/{} на получение списка всех комментариев событию с id = {}", eventId, eventId);

        return new ResponseEntity<>(commentService.getAllByEvent(eventId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long commentId) {
        log.debug("Получен Get-запрос к эндпоинту: /comments/{} на получение комментария с id = {}", commentId, commentId);

        return new ResponseEntity<>(commentService.getById(commentId), HttpStatus.OK);
    }
}
