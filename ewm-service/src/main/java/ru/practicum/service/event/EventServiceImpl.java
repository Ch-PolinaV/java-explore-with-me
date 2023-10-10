package ru.practicum.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataIntegrityViolationException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.enums.Sort;
import ru.practicum.model.enums.State;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.location.Location;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.model.DateTimeConstants.END;
import static ru.practicum.model.DateTimeConstants.START;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        log.info("Добавление нового события пользователем с id = {}", userId);

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        User user = userService.getById(userId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        Location location = locationRepository.save(locationMapper.toLocation(newEventDto.getLocation()));
        Event event = eventMapper.toEvent(user, category, newEventDto);

        event.setLocation(location);
        eventRepository.save(event);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0L);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        log.info("Изменение события с id = {} добавленного текущим пользователем с id = {}", eventId, userId);

        userService.getById(userId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new DataIntegrityViolationException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(updateEvent.getCategory()));
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Некорректное значение даты");
            }
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ForbiddenException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
            } else {
                event.setEventDate(updateEvent.getEventDate());
            }
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        log.info("Редактирование данных события c id = {} администратором.", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));

        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(updateEvent.getCategory()));
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Некорректное значение даты");
            } else {
                event.setEventDate(updateEvent.getEventDate());
            }
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.save(locationMapper.toLocation(updateEvent.getLocation()));
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (event.getState() != State.PENDING) {
            throw new DataIntegrityViolationException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(State.REJECTED);
                    break;
            }
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        log.info("Получение события с id = {}", eventId);

        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));
    }

    @Override
    public Set<Event> getAllByIds(Set<Long> eventIds) {
        log.info("Получение списка всех событий");

        return eventRepository.findEventsByIds(eventIds);
    }

    @Override
    public List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        log.info("Получение списка всех событий добавленных текущим пользователем с id = {}", userId);

        userService.getById(userId);

        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventInfoByInitiatorId(Long userId, Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем с id = {}", userId);

        userService.getById(userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(AdminEventParamsDto params) {
        log.info("Получение полной информации обо всех событиях подходящих под переданные условия");

        LocalDateTime rangeStart = params.getRangeStart();
        LocalDateTime rangeEnd = params.getRangeEnd();
        Integer from = params.getFrom();
        Integer size = params.getSize();
        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);

        if (rangeEnd != null && rangeStart != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Дата начала не может быть позже чем дата окончания события");
            }
        }

        List<EventFullDto> eventFullDtos = eventRepository.findAdminEvents(params.getUsers(), params.getStates(), params.getCategories(), rangeStart, rangeEnd, page).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());

        eventFullDtos.forEach(event -> event.setConfirmedRequests((long) requestRepository
                .findByEventIdConfirmed(event.getId()).size()));

        return eventFullDtos;
    }

    @Override
    public EventFullDto getPublicEvent(Long eventId, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено!"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие с id = " + eventId + "не найдено");
        }
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests((long) requestRepository.findByEventIdConfirmed(event.getId()).size());
        statsClient.save(toEndpointHit(request));

        return getViews(Collections.singletonList(eventFullDto)).get(0);
    }

    @Override
    public List<EventShortDto> getPublicEvents(PublicEventParamsDto params, HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации");

        String text = params.getText();
        List<Long> categories = params.getCategories();
        LocalDateTime rangeStart = params.getRangeStart();
        LocalDateTime rangeEnd = params.getRangeEnd();
        Integer from = params.getFrom();
        Integer size = params.getSize();
        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);

        if (text == null) {
            text = "";
        }
        if (categories != null && !categories.isEmpty()) {
            List<Long> checkCategories = categories.stream()
                    .filter(categoryId -> !categoryService.existsById(categoryId))
                    .collect(Collectors.toList());

            if (!checkCategories.isEmpty()) {
                throw new BadRequestException("В запросе переданы некорректные значения категорий");
            }
        }

        List<Event> events = eventRepository.findPublicEvents(text.toLowerCase(), List.of(State.PUBLISHED), categories,
                params.getPaid(), rangeStart, rangeEnd, page);

        List<EventFullDto> eventFullDtos = events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        eventFullDtos.forEach(event -> event.setConfirmedRequests((long) requestRepository
                .findByEventIdConfirmed(event.getId()).size()));

        if (params.getOnlyAvailable()) {
            eventFullDtos = eventFullDtos.stream()
                    .filter(event -> event.getParticipantLimit() <= event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }

        statsClient.save(toEndpointHit(request));

        List<EventShortDto> eventShortDtos = getViews(eventFullDtos).stream()
                .map(eventMapper::toShortFromFullDto)
                .collect(Collectors.toList());

        if (params.getSort() != null && params.getSort().equals(Sort.VIEWS)) {
            eventShortDtos.sort((e1, e2) -> e2.getViews().compareTo(e1.getViews()));
        }
        getConfirmedRequests(eventFullDtos);
        return eventShortDtos;
    }

    private EndpointHitDto toEndpointHit(HttpServletRequest request) {
        return new EndpointHitDto(null,
                "ewm-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
    }

    private List<EventFullDto> getViews(List<EventFullDto> eventDtos) {
        Map<String, EventFullDto> views = eventDtos.stream()
                .collect(Collectors.toMap(eventFullDto -> "/events/" + eventFullDto.getId(),
                        eventFullDto -> eventFullDto));

        Object responseBody = statsClient.getStats(START, END, new ArrayList<>(views.keySet()), true)
                .getBody();

        List<ViewStatsDto> viewStatsDtos = new ObjectMapper().convertValue(responseBody, new TypeReference<>() {
        });
        viewStatsDtos.forEach(viewStatsDto -> {
            if (views.containsKey(viewStatsDto.getUri())) {
                views.get(viewStatsDto.getUri()).setViews(viewStatsDto.getHits());
            }
        });

        return new ArrayList<>(views.values());
    }

    private void getConfirmedRequests(List<EventFullDto> eventDtos) {
        List<Long> ids = eventDtos.stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());
        List<Request> requests = requestRepository.findConfirmedToListEvents(ids);
        Map<Long, Long> counter = new HashMap<>();
        requests.forEach(element -> counter.put(element.getEvent().getId(),
                counter.getOrDefault(element.getEvent().getId(), 0L) + 1));
        eventDtos.forEach(event -> event.setConfirmedRequests(counter.get(event.getId())));
    }
}