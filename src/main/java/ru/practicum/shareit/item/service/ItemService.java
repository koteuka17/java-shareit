package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItemDto(Long userId, ItemDto itemDto);

    ItemDto updateItemDto(Long userId, ItemDto itemDto, Long id);

    List<ItemDto> getItemsDto(Long userId);

    ItemDto getItemDto(Long id);

    List<ItemDto> searchItem(Long userId, String text);
}
