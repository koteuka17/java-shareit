package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;        //— уникальный идентификатор пользователя

    @NotBlank(message = "Имя не может быть пустым")
    private String name;    //— имя или логин пользователя

    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;   //— адрес электронной почты
}
