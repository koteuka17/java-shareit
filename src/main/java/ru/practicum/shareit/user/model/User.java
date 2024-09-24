package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;        //— уникальный идентификатор пользователя

    @Column(name = "name")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;    //— имя или логин пользователя

    @Column(name = "email", unique = true)
    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;   //— адрес электронной почты
}
