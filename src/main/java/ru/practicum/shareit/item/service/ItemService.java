package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, Item item, Long id);

    List<Item> getItems(Long userId);

    ItemDto getItemDto(Long id);

    Item getItem(Long id);

    List<ItemDto> searchItem(Long userId, String text);
}
