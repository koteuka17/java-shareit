package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    List<User> getAll();

    List<UserDto> getAllDto();

    UserDto getUser(Long id);

    User updUser(Long id, User user);

    void delUser(Long id);
}
