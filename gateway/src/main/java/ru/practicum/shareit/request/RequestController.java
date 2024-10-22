package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    //добавить новый запрос вещи
    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                                    @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Создание запроса пользователем с id: {}", userId);
        return requestClient.createItemRequest(userId, requestDto);
    }

    //получить список своих запросов вместе с данными об ответах на них
    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Получение всех запросов пользователя с id: {}", userId);
        return requestClient.getItemRequestsByUser(userId);
    }

    //получить список запросов, созданных другими пользователями
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получение запросов других пользователей пользователем с id: {}", userId);
        return requestClient.getAll(userId, from, size);
    }

    //получить данные об одном конкретном запросе вместе с данными об ответах на него
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(@PathVariable("id") @Min(1) Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId) {
        log.info("Получение запроса с id: {}", requestId);
        return requestClient.getItemRequest(requestId, userId);
    }
}
