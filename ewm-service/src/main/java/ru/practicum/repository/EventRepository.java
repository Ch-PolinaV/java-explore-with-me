package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.enums.State;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.id IN :eventIds")
    Set<Event> findEventsByIds(Set<Long> eventIds);

    @Query("SELECT e, COUNT(c) FROM Event e LEFT JOIN Comment c ON e.id = c.event.id " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (coalesce(:rangeStart, null) is null OR e.eventDate >= :rangeStart) " +
            "AND (coalesce(:rangeEnd, null) is null OR e.eventDate <= :rangeEnd) " +
            "GROUP BY e ORDER BY e.createdOn DESC")
    List<Object[]> findAdminEvents(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e, (SELECT COUNT(c) FROM Comment c WHERE c.event = e) as commentCount " +
            "FROM Event AS e " +
            "WHERE " +
            "(" +
            ":text IS NULL " +
            "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') " +
            "OR LOWER(e.annotation) LIKE CONCAT('%', :text, '%')" +
            ")" +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd) " +
            "ORDER BY e.eventDate")
    List<Object[]> findPublicEvents(
            @Param("text") String text,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("SELECT e, COUNT(c) FROM Event e LEFT JOIN Comment c ON e.id = c.event.id WHERE e.initiator.id = :userId GROUP BY e")
    List<Object[]> findEventsWithCommentCounts(@Param("userId") Long userId, Pageable page);
}