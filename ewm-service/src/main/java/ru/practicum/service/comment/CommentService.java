package ru.practicum.service.comment;

import ru.practicum.model.comment.dto.CommentCreateDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, CommentCreateDto commentCreateDto);

    CommentDto update(Long userId, CommentUpdateDto commentUpdateDto);

    CommentDto updateByAdmin(Long commentId, CommentUpdateDto commentUpdateDto);

    CommentDto getById(Long commentId);

    List<CommentDto> getAllByUser(Long userId, Integer from, Integer size);

    List<CommentDto> getAllByEvent(Long eventId, Integer from, Integer size);

    void deleteByUser(Long userId, Long commentId);

    void deleteByAdmin(Long commentId);
}
