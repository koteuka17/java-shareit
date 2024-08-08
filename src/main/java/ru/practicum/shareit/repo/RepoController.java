package ru.practicum.shareit.repo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RestController
@AllArgsConstructor
public class RepoController {
    private final RepoService service;

    //создание пользователя
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    //получение пользователя
    @GetMapping("/users/{id}")
    public UserDto findUser(@PathVariable long id) {
        return service.getUser(id);
    }

    //обновление пользователя
    @PatchMapping("/users/{id}")
    public User updateUser(@RequestBody User user,
                           @PathVariable("id") @Min(1) Long id) {
        return service.updUser(id, user);
    }

    //удаление пользователя по id
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        service.delUser(id);
    }

    //создание вещи
    @PostMapping("/items")
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        return service.createItem(userId, itemDto);
    }

    //изменение вещи
    @PatchMapping("/items/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                           @RequestBody Item item,
                           @PathVariable("id") @Min(1) Long id) {
        return service.updateItem(userId, item, id);
    }

    //получение всех вещей пользователя
    @GetMapping("/items")
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        return service.getItems(userId);
    }

    //получение вещи по id
    @GetMapping("/items/{id}")
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                            @PathVariable Long id) {
        return service.getItemDto(id);
    }

    //поиск вещи по тексту
    @GetMapping("/items/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(required = false) String text) {
        return service.searchItem(userId, text);
    }
}
