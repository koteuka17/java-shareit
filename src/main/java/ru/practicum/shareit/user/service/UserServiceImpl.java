package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto addUserDto(UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        User user = UserMapper.toUser(userDto);
        if (repository.getAll().contains(user)) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException("Такой пользователь уже существует");
        }
        return UserMapper.toUserDto(repository.addUser(user));
    }

    @Override
    public UserDto getUserDto(Long id) {
        log.info("Получение пользователя с id: {}", id);
        return UserMapper.toUserDto(repository.getUser(id));
    }

    @Override
    public UserDto updUserDto(Long id, UserDto userDto) {
        log.info("Обновление пользователя c id: {} - {}", id, userDto);
        return UserMapper.toUserDto(repository.updUser(id, UserMapper.toUser(userDto)));
    }

    @Override
    public void delUserDto(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        repository.delUser(id);
    }
}
