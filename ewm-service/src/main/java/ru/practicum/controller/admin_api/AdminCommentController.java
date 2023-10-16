package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.CommentUpdateDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long commentId,
                                             @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        log.debug("Получен Patch-запрос к эндпоинту: /admin/comments/{} на изменение комментария администратором", commentId);

        return new ResponseEntity<>(commentService.updateByAdmin(commentId, commentUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        log.debug("Получен Delete-запрос к эндпоинту: /admin/comments/{} на удаление комментария с id = {}", commentId, commentId);

        commentService.deleteByAdmin(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}