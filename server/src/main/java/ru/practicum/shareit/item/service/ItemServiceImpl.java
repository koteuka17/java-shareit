package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemDtoOut createItemDto(Long userId, ItemDto itemDto) {
        log.info("Создание вещи {}", itemDto);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не существует"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        Long requestId = itemDto.getRequestId();
        if (requestId != null && requestId != 0L) {
            item.setRequest(requestRepository.findById(requestId).orElseThrow(() ->
                    new EntityNotFoundException("Запрос не найден")));
        }
        return ItemMapper.toItemDtoOut(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItemDto(Long userId, ItemDto itemDto, Long id) {
        log.info("Обновление вещи с id: {} - {}", id, itemDto);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не существует"));
        Item savedItem = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь не найдена"));
        if (!savedItem.getOwner().equals(user)) {
            throw new NotFoundException("Изменять данные о вещи может только ее владелец");
        }
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name != null && !name.isBlank()) {
            savedItem.setName(name);
        }
        if (description != null && !description.isBlank()) {
            savedItem.setDescription(description);
        }
        if (available != null) {
            savedItem.setAvailable(available);
        }
        itemRepository.save(savedItem);
        return ItemMapper.toItemDtoOut(savedItem);
    }

    @Override
    public List<ItemDtoOut> getItemsDto(Long userId) {
        log.info("Просмотр владельцем с id: {} списка всех его вещей", userId);
        return itemRepository.findAllByOwnerIdOrderByIdAsc(userId).stream()
                .map(ItemMapper::toItemDtoOut)
                .collect(toList());
    }

    @Override
    public ItemDtoOut getItemDto(Long id, Long userId) {
        log.info("Получение вещи с id: {}", id);
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена"));
        ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(item);

        LocalDateTime thisMoment = LocalDateTime.now();
        if (Objects.equals(itemDtoOut.getOwner().getId(), userId)) {
            itemDtoOut.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartLessThanEqualAndStatus(itemDtoOut.getId(), thisMoment,
                            Status.APPROVED, Sort.by(DESC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));

            itemDtoOut.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatus(itemDtoOut.getId(), thisMoment,
                            Status.APPROVED, Sort.by(ASC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));
        }

        itemDtoOut.setComments(commentRepository.findAllByItemId(itemDtoOut.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(toList()));

        return itemDtoOut;
    }

    @Override
    public List<ItemDtoOut> searchItem(Long userId, String text) {
        log.info("Поиск вещи пользователя с id: {} - по тексту: {}", userId, text);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDtoOut)
                .filter(ItemDtoOut::getAvailable)
                .toList();
    }

    @Override
    public CommentDto createCommentDto(Long userId, Long itemId, CommentDto commentDto) {
        log.info("Создание комментария {}", commentDto);
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new IllegalArgumentException("Пользователь не пользовался вещью");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена"));

        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, item, user)));
    }
}
