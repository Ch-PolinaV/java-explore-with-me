package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataIntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории: {}", newCategoryDto);

        if (newCategoryDto.getName() != null) {
            String name = newCategoryDto.getName();

            if (categoryRepository.existsByName(name)) {
                throw new DataIntegrityViolationException("Категория с именем '" + name + "' уже существует.");
            }
        }

        return mapper.toCategoryDto(categoryRepository.save(mapper.newCategoryDtoToCategory(newCategoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        log.info("Изменение категории с id = {}", catId);

        if (categoryDto.getId() == null) {
            categoryDto.setId(catId);
        }

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена!"));

        if (categoryDto.getName() != null) {
            String name = categoryDto.getName();

            if (!name.equals(category.getName()) && categoryRepository.existsByName(name)) {
                throw new DataIntegrityViolationException("Категория с именем '" + name + "' уже существует.");
            }

            categoryDto.setName(name);
        }

        return mapper.toCategoryDto(categoryRepository.save(mapper.categoryDtoToCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);

        log.info("Получение списка всех категорий");
        return categoryRepository.findAll(page).stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long catId) {
        log.info("Получение категории с id = {}", catId);
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена!"));
    }

    @Override
    public CategoryDto getCategoryDtoById(Long catId) {
        log.info("Получение категории с id = {}", catId);

        return mapper.toCategoryDto(getCategoryById(catId));
    }

    @Override
    public Boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long catId) {
        log.info("Удаление категории с id = {}", catId);

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + catId + " не найдена!"));

        List<Event> events = eventRepository.findByCategory(category);
        if (!events.isEmpty()) {
            throw new DataIntegrityViolationException("Невозможно удалить категорию, так как она связана с событиями.");
        }

        categoryRepository.deleteById(catId);
    }
}