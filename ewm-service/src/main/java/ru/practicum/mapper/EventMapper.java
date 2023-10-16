package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.NewEventDto;
import ru.practicum.model.user.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class, LocationMapper.class})
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(category)")
    @Mapping(target = "state", constant = "PENDING")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "publishedOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event toEvent(User initiator, Category category, NewEventDto newEventDto);

    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "views", expression = "java(0L)")
    @Mapping(target = "comments", expression = "java(0L)")
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "comments", expression = "java(0L)")
    EventShortDto toEventShortDto(Event event);

    EventShortDto toShortFromFullDto(EventFullDto eventFullDto);
}