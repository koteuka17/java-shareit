package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //создание вещи
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    //изменение вещи
    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                           @RequestBody Item item,
                           @PathVariable("id") @Min(1) Long id) {
        return itemService.updateItem(userId, item, id);
    }

    //получение всех вещей пользователя
    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        return itemService.getItems(userId);
    }

    //получение вещи по id
    @GetMapping("/{id}")
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                            @PathVariable Long id) {
        return itemService.getItemDto(id);
    }

    //поиск вещи по тексту
    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(required = false) String text) {
        return itemService.searchItem(userId, text);
    }
}
