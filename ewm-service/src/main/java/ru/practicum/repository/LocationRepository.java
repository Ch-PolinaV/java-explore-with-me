package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}