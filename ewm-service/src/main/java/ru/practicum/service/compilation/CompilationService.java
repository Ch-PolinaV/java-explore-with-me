package ru.practicum.service.compilation;

import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);

    void deleteById(Long compId);
}