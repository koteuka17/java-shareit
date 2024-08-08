package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, Item item, Long id);

    List<Item> getAllItems();

    List<ItemDto> getAllItemsDto();

    List<Item> getItems(Long userId);

    ItemDto getItemDto(Long id);

    Item getItem(Long id);

    List<ItemDto> searchItem(Long userId, String text);
}
