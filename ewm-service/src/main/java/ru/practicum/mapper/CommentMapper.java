package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.dto.CommentCreateDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.CommentUpdateDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentCreateDto.text")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "event", source = "event")
    Comment createToComment(User user, Event event, CommentCreateDto commentCreateDto);

    @Mapping(target = "id", source = "commentUpdateDto.commentId")
    @Mapping(target = "text", source = "commentUpdateDto.text")
    @Mapping(target = "createdOn", source = "createdOn")
    @Mapping(target = "updatedOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "event", source = "event")
    Comment updateToComment(User user, Event event, LocalDateTime createdOn, CommentUpdateDto commentUpdateDto);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "authorId", source = "author.id")
    CommentDto toCommentDto(Comment comment);
}