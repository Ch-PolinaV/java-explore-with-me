package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.enums.Status;
import ru.practicum.model.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findByEventIdAndStatus(Long eventId, Status status);

    List<Request> findByStatusAndEventIdIn(Status status, List<Long> events);

    List<Request> findByEventIdAndIdIn(Long eventId, List<Long> requestIds);

    @Query("select request from Request request " +
            "where request.event.id = :eventId " +
            "and request.event.initiator.id = :userId")
    List<Request> findByEventIdAndInitiatorId(@Param("eventId") Long eventId,
                                              @Param("userId") Long userId);
}