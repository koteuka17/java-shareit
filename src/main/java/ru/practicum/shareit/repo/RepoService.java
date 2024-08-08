package ru.practicum.shareit.repo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RepoService {
    private Repo repository;

    public User addUser(User user) {
        log.info("Создание пользователя {}", user);
        if (repository.getAllUsers().contains(user)) {
            log.warn("Такой пользователь уже существует");
            throw new ValidationException("Такой пользователь уже существует");
        }
        return repository.addUser(user);
    }

    public UserDto getUser(Long id) {
        log.info("Получение пользователя с id: {}", id);
        return repository.getUser(id);
    }

    public User updUser(Long id, User user) {
        log.info("Обновление пользователя c id: {} - {}", id, user);
        return repository.updUser(id, user);
    }

    public void delUser(Long id) {
        log.info("Удаление пользователя с id: {}", id);
        repository.delUser(id);
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Создание вещи {}", itemDto);
        if (repository.getAllItemsDto().contains(itemDto)) {
            log.warn("Такая вещь уже существует");
            throw new ValidationException("Такая вещь уже существует");
        }
        return repository.createItem(userId, itemDto);
    }

    public Item updateItem(Long userId, Item item, Long id) {
        log.info("Обновление вещи с id: {} - {}", id, item);
        if (!repository.getItem(id).getOwner().equals(userId)) {
            throw new NotFoundException("Изменять данные о вещи может только ее владелец");
        }
        return repository.updateItem(userId, item, id);
    }

    public List<Item> getItems(Long userId) {
        log.info("Просмотр владельцем с id: {} списка всех его вещей", userId);
        return repository.getItems(userId);
    }

    public ItemDto getItemDto(Long id) {
        log.info("Получение вещи с id: {}", id);
        return repository.getItemDto(id);
    }

    public Item getItem(Long id) {
        log.info("Получение вещи с id: {}", id);
        return repository.getItem(id);
    }

    public List<ItemDto> searchItem(Long userId, String text) {
        log.info("Поиск вещи пользователя с id: {} - по тексту: {}", userId, text);
        return repository.searchItem(userId, text);
    }
}
