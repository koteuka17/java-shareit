package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;        //— уникальный идентификатор пользователя
    private String name;    //— имя или логин пользователя
    private String email;   //— адрес электронной почты
}
