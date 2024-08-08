package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, Item> storage = new HashMap<>();
    private final HashMap<Long, ItemDto> storageDto = new HashMap<>();
    private long count = 1;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        itemDto.setId(count);
        Item item = ItemMapper.fromItemDto(itemDto, userId);
        storage.put(count, item);
        storageDto.put(count++, itemDto);
        return itemDto;
    }

    @Override
    public Item updateItem(Long userId, Item item, Long id) {
        Item savedItem = storage.get(id);
        savedItem.setName(item.getName());
        savedItem.setDescription(item.getDescription());
        savedItem.setAvailable(item.getAvailable());
        savedItem.setRequest(item.getRequest());
        storage.put(id, savedItem);
        storageDto.put(id, ItemMapper.toItemDto(savedItem));
        return savedItem;
    }

    @Override
    public List<Item> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<ItemDto> getAllDto() {
        return List.copyOf(storageDto.values());
    }

    @Override
    public List<Item> getItems(Long userId) {
        return storage.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemDto(Long id) {
        return storageDto.get(id);
    }

    @Override
    public Item getItem(Long id) {
        return storage.get(id);
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> result = new ArrayList<>();
        Collection<ItemDto> list = storageDto.values();
        for (ItemDto item : list) {
            if (item.getName() != null) {
                if (item.getDescription() != null) {
                    if (item.getAvailable()) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                            result.add(item);
                    }
                }
            }
        }
        return result;
    }
}
