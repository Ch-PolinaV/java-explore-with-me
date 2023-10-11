package ru.practicum.controller.public_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(@RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Получен Get-запрос к эндпоинту: /categories на получение списка всех категорий");

        return new ResponseEntity<>(categoryService.getAll(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        log.debug("Получен Get-запрос к эндпоинту: /categories/{} на получение категории с id = {}", catId, catId);

        return new ResponseEntity<>(categoryService.getCategoryDtoById(catId), HttpStatus.OK);
    }
}