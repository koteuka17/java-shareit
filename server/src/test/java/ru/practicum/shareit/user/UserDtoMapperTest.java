package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoShort;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserDtoMapperTest {
    private final UserDto dto = new UserDto(1L, "user", "user@email.ru");
    private final User user = new User(1L, "user", "user@email.ru");
    private final UserDtoShort userShort = new UserDtoShort(1L, "user");

    @Test
    public void toUserDto() {
        UserDto userDto = UserMapper.toUserDto(user);
        assertThat(userDto, equalTo(dto));
    }

    @Test
    public void toUserDtoShort() {
        UserDtoShort userDtoShort = UserMapper.toUserDtoShort(user);
        assertThat(userDtoShort, equalTo(userShort));
    }

    @Test
    public void toUser() {
        User user = UserMapper.toUser(dto);
        assertThat(user.getId(), equalTo(this.user.getId()));
        assertThat(user.getName(), equalTo(this.user.getName()));
        assertThat(user.getEmail(), equalTo(this.user.getEmail()));
    }
}
