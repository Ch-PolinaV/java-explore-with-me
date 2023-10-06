package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataIntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.model.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.model.user.User;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        log.info("Добавление запроса на участие в событии с id = {} пользователем с id = {}", eventId, userId);

        User user = userService.getById(userId);
        Event event = eventService.getEventById(eventId);

        if (event.getState() != (State.PUBLISHED)) {
            throw new DataIntegrityViolationException("Нельзя подать запрос на участие в неопубликованном событии");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        int confirmedRequests = requestRepository.findByEventIdConfirmed(eventId).size();

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new DataIntegrityViolationException("Достигнут лимит запросов на участие");
        }

        Status status = Status.PENDING;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = Status.CONFIRMED;
        }

        Request request = new Request(null, LocalDateTime.now(), event, user, status);
        Optional<Request> check = requestRepository.findByEventIdAndRequesterId(eventId, userId);

        if (check.isPresent()) {
            throw new DataIntegrityViolationException("Нельзя добавить повторный запрос");
        }

        request = requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest) {
        Event event = eventService.getEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Статус события может изменить только его владелец");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new DataIntegrityViolationException("Нарушение целостности данных");
        }

        EventRequestStatusUpdateResult updateRequest = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        int confirmedRequests = requestRepository.findByEventIdConfirmed(eventId).size();
        List<Request> requests = requestRepository.findByEventIdAndRequestsIds(eventId, eventRequest.getRequestIds());

        if (Objects.equals(eventRequest.getStatus(), Status.CONFIRMED)
                && confirmedRequests + requests.size() > event.getParticipantLimit()) {
            requests.forEach(request -> request.setStatus(Status.REJECTED));
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(requestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            updateRequest.setRejectedRequests(requestDto);
            requestRepository.saveAll(requests);
            throw new DataIntegrityViolationException("Превышен лимит запросов");
        }

        if (eventRequest.getStatus().equals(Status.REJECTED)) {
            requests.forEach(request -> {
                if (request.getStatus().equals(Status.CONFIRMED)) {
                    throw new DataIntegrityViolationException("Невозможно отклонить подтвержденный запрос");
                }
                request.setStatus(Status.REJECTED);
            });
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(requestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            updateRequest.setRejectedRequests(requestDto);
            requestRepository.saveAll(requests);
        } else if (eventRequest.getStatus().equals(Status.CONFIRMED)
                && eventRequest.getRequestIds().size() <= event.getParticipantLimit() - confirmedRequests) {
            requests.forEach(request -> request.setStatus(Status.CONFIRMED));
            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(requestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            updateRequest.setConfirmedRequests(requestDto);
            requestRepository.saveAll(requests);
        }
        return updateRequest;
    }

    @Override
    public List<ParticipationRequestDto> getByRequester(Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях");

        userService.getById(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя");

        return requestRepository.findByEventIdAndInitiatorId(eventId, userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        log.info("Отмена своего запроса на участие в событии");

        userService.getById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id = " + requestId + " не найден!"));
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(request);
    }
}