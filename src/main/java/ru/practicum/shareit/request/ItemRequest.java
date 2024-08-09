package ru.practicum.shareit.request;


import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {
    Long id;                // — уникальный идентификатор запроса
    String description;     // — текст запроса, содержащий описание требуемой вещи
    User requestor;         // — пользователь, создавший запрос
    LocalDateTime created;  // — дата и время создания запроса
}
