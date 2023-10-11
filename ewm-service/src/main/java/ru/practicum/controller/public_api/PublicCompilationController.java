package ru.practicum.controller.public_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(required = false) Boolean pinned,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен Get-запрос к эндпоинту: /compilations на получение списка всех подборок событий");

        return new ResponseEntity<>(compilationService.getAll(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getById(@PathVariable Long compId) {
        log.debug("Получен Get-запрос к эндпоинту: /compilations/{} на получение подборки событий с id = {}", compId, compId);

        return new ResponseEntity<>(compilationService.getById(compId), HttpStatus.OK);
    }
}
