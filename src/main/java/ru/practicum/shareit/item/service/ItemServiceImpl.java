package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItemDto(Long userId, ItemDto itemDto) {
        log.info("Создание вещи {}", itemDto);
        User user = userRepository.getUser(userId);
        if (user == null) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(repository.createItem(item));
    }

    @Override
    public ItemDto updateItemDto(Long userId, ItemDto itemDto, Long id) {
        log.info("Обновление вещи с id: {} - {}", id, itemDto);
        User user = userRepository.getUser(userId);
        if (user == null) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        Item item = ItemMapper.toItem(itemDto, user);
        if (!repository.getItem(id).getOwner().equals(user)) {
            throw new NotFoundException("Изменять данные о вещи может только ее владелец");
        }
        return ItemMapper.toItemDto(repository.updateItem(userId, item, id));
    }

    @Override
    public List<ItemDto> getItemsDto(Long userId) {
        log.info("Просмотр владельцем с id: {} списка всех его вещей", userId);
        return repository.getItems(userRepository.getUser(userId)).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemDto(Long id) {
        log.info("Получение вещи с id: {}", id);
        return ItemMapper.toItemDto(repository.getItem(id));
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        log.info("Поиск вещи пользователя с id: {} - по тексту: {}", userId, text);
        return repository.searchItem(userId, text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
