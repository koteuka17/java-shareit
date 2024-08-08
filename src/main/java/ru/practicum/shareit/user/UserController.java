package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    //создание пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    //получение пользователя
    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    //обновление пользователя
    @PatchMapping("/{id}")
    public User updateUser(@RequestBody User user,
                           @PathVariable("id") @Min(1) Long id) {
        return userService.updUser(id, user);
    }

    //удаление пользователя по id
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delUser(id);
    }
}
