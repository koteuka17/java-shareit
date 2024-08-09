package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto addUserDto(UserDto userDto);

    UserDto getUserDto(Long id);

    UserDto updUserDto(Long id, UserDto userDto);

    void delUserDto(Long id);
}
