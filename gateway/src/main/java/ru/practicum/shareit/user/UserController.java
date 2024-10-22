package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") @Min(1) Long id) {
        log.info("Получение пользователя с id: {}", id);
        return userClient.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @PathVariable("id") @Min(1) Long id) {
        log.info("Обновление пользователя с id: {}", id);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") @Min(1) Long id) {
        log.info("Удаление пользователя с id: {}", id);
        userClient.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получение всех пользователей");
        return userClient.getUsers();
    }
}
