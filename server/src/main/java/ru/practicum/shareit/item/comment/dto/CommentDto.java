package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    @NotBlank
    @Size
    private String text;
    private String authorName;

    @FutureOrPresent
    private LocalDateTime created;

    public CommentDto(String text) {
        this.text = text;
    }
}
