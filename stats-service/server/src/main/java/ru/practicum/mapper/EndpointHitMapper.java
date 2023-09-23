package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getId(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}
