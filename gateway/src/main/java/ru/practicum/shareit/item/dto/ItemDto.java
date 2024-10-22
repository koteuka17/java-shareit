package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    long id;
    @NotEmpty
    String name;
    @NotEmpty
    String description;
    @NotNull
    Boolean available;
    long requestId;
}
