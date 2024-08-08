package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    @Override
    public User addUser(User user) {
        log.info("Создание пользователя {}", user);
        if (repository.getAll().contains(user)) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException("Такой пользователь уже существует");
        }
        return repository.addUser(user);
    }

    @Override
    public UserDto getUser(Long id) {
        log.info("Получение пользователя с id: {}", id);
        return repository.getUser(id);
    }

    @Override
    public User updUser(Long id, User user) {
        log.info("Обновление пользователя c id: {} - {}", id, user);
        return repository.updUser(id, user);
    }

    @Override
    public void delUser(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        repository.delUser(id);
    }
}
