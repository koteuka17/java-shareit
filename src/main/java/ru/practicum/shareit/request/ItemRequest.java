package ru.practicum.shareit.request;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;                // — уникальный идентификатор запроса
    String description;     // — текст запроса, содержащий описание требуемой вещи
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requestor;         // — пользователь, создавший запрос
    LocalDateTime created;  // — дата и время создания запроса
}
