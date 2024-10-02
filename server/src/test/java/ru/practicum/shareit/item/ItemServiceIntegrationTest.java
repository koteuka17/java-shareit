package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void saveNewItem() {
        UserDto userDto = new UserDto(null, "name", "name@email.ru");
        ItemDto itemDtoIn = new ItemDto("item", "description", true, null);

        UserDto user = userService.addUserDto(userDto);
        itemService.createItemDto(user.getId(), itemDtoIn);

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name like :item", Item.class);
        Item item = queryItem.setParameter("item", itemDtoIn.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDtoIn.getName()));
        assertThat(item.getDescription(), equalTo(itemDtoIn.getDescription()));
    }
}