package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }

    public static ItemDtoShort toItemDtoShort(Item item) {
        return new ItemDtoShort(
                item.getId(),
                item.getName()
        );
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user
        );
    }

    public static ItemDtoOut toItemDtoOut(Item item) {
        ItemDtoOut itemDtoOut = new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDtoShort(item.getOwner())
        );
        if (item.getRequest() != null) {
            itemDtoOut.setRequestId(item.getRequest().getId());
        }
        return itemDtoOut;
    }
}
