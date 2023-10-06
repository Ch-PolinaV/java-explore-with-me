package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("Получен POST-запрос к эндпоинту: /admin/categories на сохранение новой категории");

        return new ResponseEntity<>(categoryService.create(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long catId,
                                              @Valid @RequestBody CategoryDto categoryDto) {
        log.debug("Получен Patch-запрос к эндпоинту: /admin/categories/{} на изменение категории с id = {}", catId, catId);

        return new ResponseEntity<>(categoryService.update(catId, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        log.debug("Получен Delete-запрос к эндпоинту: /admin/categories/{} на удаление категории с id = {}", catId, catId);

        categoryService.deleteById(catId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}