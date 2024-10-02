package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    //создание вещи
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Создание вещи");
        return itemClient.createItem(userId, itemDto);
    }

    //изменение вещи
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable("id") @Min(1) Long id,
                                             @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Обновление вещи с id: {}", id);
        return itemClient.updateItem(itemDto, id, userId);
    }

    //получение всех вещей пользователя
    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получение вещей пользователя с id: {}", userId);
        return itemClient.getItems(userId, from, size);
    }

    //получение вещи по id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Long id,
                                          @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Получение вещи с id: {}", id);
        return itemClient.getItem(id, userId);
    }

    //поиск вещи по тексту
    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false) String text,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("Поиск вещей по тексту: {}", text);
        return itemClient.searchItem(userId, text, from, size);
    }

    //удаление вещи
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") @Min(1) Long id) {
        log.info("Удаление вещи с id: {}", id);
        return itemClient.deleteItem(id);
    }

    //добавление отзыва
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") @Min(1) Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("Добавление отзыва вещи с id: {}", itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
