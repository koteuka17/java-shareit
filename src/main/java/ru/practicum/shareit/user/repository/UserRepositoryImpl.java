package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    private final HashMap<Long, UserDto> storageDto = new HashMap<>();
    private final ArrayList<String> emails = new ArrayList<>();
    private long count = 1;
    public static final ArrayList<Long> existingIds = new ArrayList<>();

    @Override
    public User addUser(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        emails.add(email);
        user.setId(count);
        storage.put(count, user);
        storageDto.put(count, UserMapper.toUserDto(user));
        existingIds.add(count++);
        return user;
    }

    @Override
    public List<UserDto> getAllDto() {
        return List.copyOf(storageDto.values());
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public UserDto getUser(Long id) {
        return storageDto.get(id);
    }

    @Override
    public User updUser(Long id, User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        User savedUser = storage.get(id);
        savedUser.setName(user.getName());
        savedUser.setEmail(email);
        storage.put(id, savedUser);
        storageDto.put(id, UserMapper.toUserDto(savedUser));
        emails.add(email);
        return savedUser;
    }

    @Override
    public void delUser(Long id) {
        emails.remove(storage.get(id).getEmail());
        storage.remove(id);
        storageDto.remove(id);
    }
}
