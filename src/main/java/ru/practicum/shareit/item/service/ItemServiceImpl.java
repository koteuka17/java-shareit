package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository repository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        log.info("Создание вещи {}", itemDto);
        if (repository.getAllDto().contains(itemDto)) {
            log.warn("Такая вещь уже существует");
            throw new ValidationException("Такая вещь уже существует");
        }
        if (!UserRepositoryImpl.existingIds.contains(userId)) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь уже существует");
        }
        return repository.createItem(userId, itemDto);
    }

    @Override
    public Item updateItem(Long userId, Item item, Long id) {
        log.info("Обновление вещи с id: {} - {}", id, item);
        if (!repository.getItem(id).getOwner().equals(userId)) {
            throw new NotFoundException("Изменять данные о вещи может только ее владелец");
        }
        return repository.updateItem(userId, item, id);
    }

    @Override
    public List<Item> getItems(Long userId) {
        log.info("Просмотр владельцем с id: {} списка всех его вещей", userId);
        return repository.getItems(userId);
    }

    @Override
    public ItemDto getItemDto(Long id) {
        log.info("Получение вещи с id: {}", id);
        return repository.getItemDto(id);
    }

    @Override
    public Item getItem(Long id) {
        log.info("Получение вещи с id: {}", id);
        return repository.getItem(id);
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        log.info("Поиск вещи пользователя с id: {} - по тексту: {}", userId, text);
        return repository.searchItem(userId, text);
    }
}
