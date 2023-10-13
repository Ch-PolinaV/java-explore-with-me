package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.dto.CommentCreateUpdateDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper mapper;

    @Override
    @Transactional
    public CommentDto create(Long userId, Long eventId, CommentCreateUpdateDto commentCreateDto) {
        log.info("Добавление комментария к событию с id = {} пользователем с id = {}", eventId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));

        return mapper.toCommentDto(commentRepository.save(mapper.toComment(user, event, commentCreateDto)));
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, CommentCreateUpdateDto commentUpdateDto) {
        log.info("Изменение комментария пользователем с id = {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден!"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id = " + commentId + " не найден!"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Изменять комментарий может только пользователь который его оставил");
        }

        return mapper.toCommentDto(commentRepository.save(mapper.toComment(user, comment.getEvent(), commentUpdateDto)));
    }

    @Override
    public CommentDto getById(Long commentId) {
        log.info("Получение комментария с id = {}", commentId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id = " + commentId + " не найден!"));

        return mapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllByUser(Long userId, Integer from, Integer size) {
        log.info("Получение списка всех комментариев пользователя с id = {}", userId);

        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден!"));

        return commentRepository.findCommentsByAuthorId(userId, page).stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllByEvent(Long eventId, Integer from, Integer size) {
        log.info("Получение списка всех комментариев события с id = {}", eventId);

        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));

        return commentRepository.findCommentsByEventId(eventId, page).stream()
                .map(mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByUser(Long userId, Long commentId) {
        log.info("Удаление комментария с id = {} пользователя с id = {}", commentId, userId);

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден!"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id = " + commentId + " не найден!"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Удалять комментарий может только пользователь который его оставил");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId) {
        log.info("Удаление комментария с id = {}", commentId);

        commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id = " + commentId + " не найден!"));

        commentRepository.deleteById(commentId);
    }
}