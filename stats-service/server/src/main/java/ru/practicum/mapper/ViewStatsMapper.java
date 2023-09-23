package ru.practicum.mapper;

import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }

    public static ViewStats toViewStats(ViewStatsDto viewStatsDto) {
        return new ViewStats(
                viewStatsDto.getApp(),
                viewStatsDto.getUri(),
                viewStatsDto.getHits()
        );
    }
}
