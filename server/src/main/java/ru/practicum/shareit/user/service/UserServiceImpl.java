package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EntityException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto addUserDto(UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        User user = UserMapper.toUser(userDto);
        if (repository.findAll().contains(user)) {
            log.warn("Такой пользователь уже существует");
            throw new EntityException("Такой пользователь уже существует");
        }
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public UserDto getUserDto(Long id) {
        log.info("Получение пользователя с id: {}", id);
        return UserMapper.toUserDto(repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return repository.findAll().stream().map(UserMapper::toUserDto).collect(toList());
    }

    @Override
    public UserDto updUserDto(Long id, UserDto userDto) {
        log.info("Обновление пользователя c id: {} - {}", id, userDto);
        User savedUser = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));
        String name = userDto.getName();
        String email = userDto.getEmail();
        if (name != null && !name.isBlank()) {
            savedUser.setName(name);
        }
        if (email != null && !email.isBlank()) {
            savedUser.setEmail(email);
        }
        repository.save(savedUser);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public void delUserDto(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        repository.deleteById(id);
    }
}
