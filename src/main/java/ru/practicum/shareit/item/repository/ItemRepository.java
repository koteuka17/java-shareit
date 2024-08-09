package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Long userId, Item item, Long id);

    List<Item> getAllItems();

    List<Item> getItems(User user);

    Item getItem(Long id);

    List<Item> searchItem(Long userId, String text);
}
