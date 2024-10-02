package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUserDto(UserDto userDto);

    UserDto getUserDto(Long id);

    List<UserDto> getAllUsers();

    UserDto updUserDto(Long id, UserDto userDto);

    void delUserDto(Long id);
}
