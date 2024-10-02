package ru.practicum.shareit.user;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //создание пользователя
    @PostMapping
    public UserDto createUserDto(@RequestBody UserDto userDto) {
        return userService.addUserDto(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    //получение пользователя
    @GetMapping("/{id}")
    public UserDto findUserDto(@PathVariable long id) {
        return userService.getUserDto(id);
    }

    //обновление пользователя
    @PatchMapping("/{id}")
    public UserDto updateUserDto(@RequestBody UserDto userDto,
                                 @PathVariable("id") @Min(1) Long id) {
        return userService.updUserDto(id, userDto);
    }

    //удаление пользователя по id
    @DeleteMapping("/{id}")
    public void deleteUserDto(@PathVariable long id) {
        userService.delUserDto(id);
    }
}
