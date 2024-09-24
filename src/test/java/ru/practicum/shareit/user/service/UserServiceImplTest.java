package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repository;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto(1L, "John Doe", "john.doe@example.com");
    }

    @Test
    void addUserDto() {
        UserDto result = userService.addUserDto(userDto);

        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());

        Optional<User> savedUser = repository.findById(result.getId());
        assertTrue(savedUser.isPresent());
        assertEquals(userDto.getName(), savedUser.get().getName());
        assertEquals(userDto.getEmail(), savedUser.get().getEmail());
    }
}