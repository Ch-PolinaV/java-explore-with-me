package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.dto.CommentCreateUpdateDto;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentCreateUpdateDto.text")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "event", source = "event")
    Comment toComment(User user, Event event, CommentCreateUpdateDto commentCreateUpdateDto);

    Comment commentDtoToComment(CommentDto commentDto);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "authorId", source = "author.id")
    CommentDto toCommentDto(Comment comment);
}