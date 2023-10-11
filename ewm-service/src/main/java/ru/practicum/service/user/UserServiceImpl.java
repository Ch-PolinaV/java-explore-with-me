package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {
        log.info("Добавление нового пользователя: {}", newUserRequest);

        if (newUserRequest.getName() != null) {
            String name = newUserRequest.getName();

            if (userRepository.existsByName(name)) {
                throw new ConflictException("Пользователь с именем '" + name + "' уже существует.");
            }
        }

        return mapper.toUserDto(userRepository.save(mapper.toUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : from, size);

        log.info("Получение списка всех пользователей");

        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(page).stream()
                    .map(mapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, page).stream()
                    .map(mapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public User getById(Long id) {
        log.info("Получение пользователя с id = {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден!"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Удаление пользователя с id = {}", id);

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден!"));

        userRepository.deleteById(id);
    }
}