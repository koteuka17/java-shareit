package ru.practicum.shareit.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Repo {
    private final HashMap<Long, Item> itemStorage = new HashMap<>();
    private final HashMap<Long, ItemDto> itemStorageDto = new HashMap<>();
    private long itemCount = 1;
    private final HashMap<Long, User> userStorage = new HashMap<>();
    private final HashMap<Long, UserDto> userStorageDto = new HashMap<>();
    private final ArrayList<String> emails = new ArrayList<>();
    private long userCount = 1;
    private final ArrayList<Long> existingIds = new ArrayList<>();

    public User addUser(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        emails.add(email);
        user.setId(userCount);
        userStorage.put(userCount, user);
        userStorageDto.put(userCount, UserMapper.toUserDto(user));
        existingIds.add(userCount++);
        return user;
    }

    public List<UserDto> getAllUsersDto() {
        return List.copyOf(userStorageDto.values());
    }

    public List<User> getAllUsers() {
        return List.copyOf(userStorage.values());
    }

    public UserDto getUser(Long id) {
        return userStorageDto.get(id);
    }

    public User updUser(Long id, User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        User savedUser = userStorage.get(id);
        savedUser.setName(user.getName());
        savedUser.setEmail(email);
        userStorage.put(id, savedUser);
        userStorageDto.put(id, UserMapper.toUserDto(savedUser));
        emails.add(email);
        return savedUser;
    }

    public void delUser(Long id) {
        emails.remove(userStorage.get(id).getEmail());
        userStorage.remove(id);
        userStorageDto.remove(id);
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (!existingIds.contains(userId)) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        itemDto.setId(itemCount);
        Item item = ItemMapper.fromItemDto(itemDto, userId);
        itemStorage.put(itemCount, item);
        itemStorageDto.put(itemCount++, itemDto);
        return itemDto;
    }

    public Item updateItem(Long userId, Item item, Long id) {
        Item savedItem = itemStorage.get(id);
        savedItem.setName(item.getName());
        savedItem.setDescription(item.getDescription());
        savedItem.setAvailable(item.getAvailable());
        savedItem.setRequest(item.getRequest());
        itemStorage.put(id, savedItem);
        itemStorageDto.put(id, ItemMapper.toItemDto(savedItem));
        return savedItem;
    }

    public List<Item> getAllItems() {
        return List.copyOf(itemStorage.values());
    }

    public List<ItemDto> getAllItemsDto() {
        return List.copyOf(itemStorageDto.values());
    }

    public List<Item> getItems(Long userId) {
        return itemStorage.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    public ItemDto getItemDto(Long id) {
        return itemStorageDto.get(id);
    }

    public Item getItem(Long id) {
        return itemStorage.get(id);
    }

    public List<ItemDto> searchItem(Long userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> result = new ArrayList<>();
        Collection<ItemDto> list = itemStorageDto.values();
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
