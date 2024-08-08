package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    User addUser(User user);

    UserDto getUser(Long id);

    User updUser(Long id, User user);

    void delUser(Long id);
}
