package ru.practicum.service.comment;

import ru.practicum.model.comment.dto.CommentCreateUpdateDto;
import ru.practicum.model.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, CommentCreateUpdateDto commentCreateUpdateDto);

    CommentDto update(Long userId, Long commentId, CommentCreateUpdateDto commentCreateUpdateDto);

    CommentDto getById(Long commentId);

    List<CommentDto> getAllByUser(Long userId, Integer from, Integer size);

    List<CommentDto> getAllByEvent(Long eventId, Integer from, Integer size);

    void deleteByUser(Long userId, Long commentId);

    void deleteByAdmin(Long commentId);
}
