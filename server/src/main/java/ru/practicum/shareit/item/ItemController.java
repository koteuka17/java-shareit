package ru.practicum.shareit.item;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //создание вещи
    @PostMapping
    public ItemDtoOut createItemDto(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                    @RequestBody ItemDto itemDto) {
        return itemService.createItemDto(userId, itemDto);
    }

    //изменение вещи
    @PatchMapping("/{id}")
    public ItemDtoOut updateItemDto(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                    @RequestBody ItemDto itemDto,
                                    @PathVariable("id") @Min(1) Long id) {
        return itemService.updateItemDto(userId, itemDto, id);
    }

    //получение всех вещей пользователя
    @GetMapping
    public List<ItemDtoOut> getAllItemsDto(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        return itemService.getItemsDto(userId);
    }

    //получение вещи по id
    @GetMapping("/{id}")
    public ItemDtoOut findItemDto(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                  @PathVariable Long id) {
        return itemService.getItemDto(id, userId);
    }

    //поиск вещи по тексту
    @GetMapping("/search")
    public List<ItemDtoOut> searchItemDto(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(required = false) String text) {
        return itemService.searchItem(userId, text);
    }

    //добавление отзыва
    @PostMapping("/{itemId}/comment")
    public CommentDto createCommentDto(@RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId,
                                       @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        return itemService.createCommentDto(userId, itemId, commentDto);
    }
}
