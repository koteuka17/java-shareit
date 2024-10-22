package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;


@UtilityClass
public class RequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto requestDto) {
        return new ItemRequest(
                requestDto.getDescription()
        );
    }

    public ItemRequestDtoOut toItemRequestDtoOut(ItemRequest request) {
        return new ItemRequestDtoOut(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }
}
