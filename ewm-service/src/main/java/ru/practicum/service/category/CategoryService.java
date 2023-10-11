package ru.practicum.service.category;

import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getAll(Integer from, Integer size);

    Category getCategoryById(Long catId);

    CategoryDto getCategoryDtoById(Long catId);

    void deleteById(Long catId);
}