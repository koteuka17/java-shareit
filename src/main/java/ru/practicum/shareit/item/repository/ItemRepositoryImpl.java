package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, Item> storage = new HashMap<>();
    private long count = 1;

    @Override
    public Item createItem(Item item) {
        item.setId(count);
        storage.put(count++, item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Item item, Long id) {
        Item savedItem = storage.get(id);
        if (item.getName() != null)
            savedItem.setName(item.getName());
        if (item.getDescription() != null)
            savedItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            savedItem.setAvailable(item.getAvailable());
        if (item.getOwner() != null)
            savedItem.setOwner(item.getOwner());
        savedItem.setRequest(item.getRequest());
        storage.put(id, savedItem);
        return savedItem;
    }

    @Override
    public List<Item> getAllItems() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<Item> getItems(User user) {
        return storage.values().stream()
                .filter(item -> item.getOwner().equals(user))
                .toList();
    }

    @Override
    public Item getItem(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Item> searchItem(Long userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> result = storage.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                .toList();
        if (result.isEmpty()) {
            result = storage.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
        return result;
    }
}
