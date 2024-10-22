package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
            objectMapper = new ObjectMapper();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания моков", e);
        }
    }

    @Test
    void createUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "name", "example@email.com");
        when(userService.addUserDto(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));

        verify(userService, times(1)).addUserDto(any(UserDto.class));
    }

    @Test
    void getAllUsers() throws Exception {
        UserDto userDto1 = new UserDto(1L, "name", "example@email.com");
        UserDto userDto2 = new UserDto(2L, "name", "example@email.com");
        List<UserDto> users = Arrays.asList(userDto1, userDto2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[1].name").value("name"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void findUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "name", "example@email.com");
        when(userService.getUserDto(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));

        verify(userService, times(1)).getUserDto(1L);
    }

    @Test
    void updateUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "name", "example@email.com");
        when(userService.updUserDto(anyLong(), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));

        verify(userService, times(1)).updUserDto(eq(1L), any(UserDto.class));
    }

    @Test
    void deleteUserDto() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).delUserDto(1L);
    }
}