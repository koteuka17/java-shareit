package ru.practicum.shareit.request.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;                // — уникальный идентификатор запроса
    private String description;     // — текст запроса, содержащий описание требуемой вещи

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;         // — пользователь, создавший запрос
    private LocalDateTime created;  // — дата и время создания запроса

    public ItemRequest(String description) {
        this.description = description;
    }
}
