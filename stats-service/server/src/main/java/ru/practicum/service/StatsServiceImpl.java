package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.EndpointHitMapper.toEndpointHit;
import static ru.practicum.mapper.EndpointHitMapper.toEndpointHitDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHitDto save(EndpointHitDto endpointHitDto) {
        return toEndpointHitDto(statsRepository.save(toEndpointHit(endpointHitDto)));
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new ValidationException("Некорректно указано время: дата окончания должна быть позже даты начала");
        }
        if (unique) {
            log.info("Получение статистики по уникальным посещениям");
            return statsRepository.findUniqueViewStats(start, end, uris).stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        } else {
            log.info("Получение статистики по посещениям без учета уникальности");
            return statsRepository.findViewStats(start, end, uris).stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        }
    }
}
