package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private long id;              //— уникальный идентификатор вещи
    private String name;          //— краткое название
    private String description;   //— развёрнутое описание
    private Boolean available;    //— статус о том, доступна или нет вещь для аренды
    private User owner;           //— владелец вещи
    private Long request;         //— id запроса другого пользователя
}
