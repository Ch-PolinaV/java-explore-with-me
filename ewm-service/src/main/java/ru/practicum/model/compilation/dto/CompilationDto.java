package ru.practicum.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    @NotNull
    private Long id;
    private Set<EventShortDto> events;
    @NotNull
    private Boolean pinned;
    @NotNull
    private String title;
}
