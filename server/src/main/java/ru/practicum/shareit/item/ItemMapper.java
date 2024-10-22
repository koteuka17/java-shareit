package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

@UtilityClass
public class ItemMapper {

    public static ItemDtoShort toItemDtoShort(Item item) {
        return new ItemDtoShort(
                item.getId(),
                item.getName()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
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
