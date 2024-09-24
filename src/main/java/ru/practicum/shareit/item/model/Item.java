package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;              //— уникальный идентификатор вещи

    @Column(name = "name")
    private String name;          //— краткое название

    @Column(name = "description")
    private String description;   //— развёрнутое описание

    @Column(name = "is_available")
    private Boolean available;    //— статус о том, доступна или нет вещь для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;           //— владелец вещи

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;         //— id запроса другого пользователя

    public Item(Long id, String name, String description, Boolean available, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
    }
}
