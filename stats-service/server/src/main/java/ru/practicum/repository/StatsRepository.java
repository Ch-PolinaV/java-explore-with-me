package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :start AND :end " +
            "AND (COALESCE(:uris, NULL) IS NULL OR eh.uri IN :uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStats> findUniqueViewStats(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :start AND :end " +
            "AND (COALESCE(:uris, NULL) IS NULL OR eh.uri IN :uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStats> findViewStats(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris
    );
}