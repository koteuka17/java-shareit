package ru.practicum.shareit.item.comment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;                        // — уникальный идентификатор комментария

    @Column(name = "feedback")
    private String text;                    // — вещь, к которой относится комментарий

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;                      // — вещь, к которой относится комментарий

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;                    // — автор комментария

    @Column(name = "created")
    private LocalDateTime created;          // — дата создания комментария
}
