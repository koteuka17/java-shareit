package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    @NotBlank(message = "Название не может быть пустым")
    private String name;          //— краткое название

    @NotBlank(message = "Описание не может быть пустым")
    private String description;   //— развёрнутое описание
    private Boolean available;    //— статус о том, доступна или нет вещь для аренды
    private Long requestId;       //— id запроса другого пользователя
}
