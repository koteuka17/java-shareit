package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
    private Long id;        //— уникальный идентификатор пользователя
    private String name;    //— имя или логин пользователя

    @NotNull(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректная электронная почта")
    private String email;   //— адрес электронной почты
}
