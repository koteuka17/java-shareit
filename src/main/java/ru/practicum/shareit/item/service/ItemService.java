package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut createItemDto(Long userId, ItemDto itemDto);

    ItemDtoOut updateItemDto(Long userId, ItemDto itemDto, Long id);

    List<ItemDtoOut> getItemsDto(Long userId);

    ItemDtoOut getItemDto(Long id, Long userId);

    List<ItemDtoOut> searchItem(Long userId, String text);

    CommentDto createCommentDto(Long userId, Long itemId, CommentDto commentDto);
}
