package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.debug("Получен POST-запрос к эндпоинту: /admin/compilations на сохранение новой подборки событий");

        return new ResponseEntity<>(compilationService.create(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.debug("Получен Patch-запрос к эндпоинту: /admin/compilations/{} на изменение подборки событий с id = {}", compId, compId);

        return new ResponseEntity<>(compilationService.update(compId, updateCompilationRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long compId) {
        log.debug("Получен Delete-запрос к эндпоинту: /admin/compilations/{} на удаление подборки событий с id = {}", compId, compId);

        compilationService.deleteById(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}