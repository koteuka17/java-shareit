package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    private final ArrayList<String> emails = new ArrayList<>();
    private long count = 1;

    @Override
    public User addUser(User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        emails.add(email);
        user.setId(count);
        storage.put(count++, user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public User getUser(Long id) {
        return storage.get(id);
    }

    @Override
    public User updUser(Long id, User user) {
        String email = user.getEmail();
        if (emails.contains(email)) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }
        User savedUser = storage.get(id);
        emails.remove(savedUser.getEmail());
        savedUser.setName(user.getName());
        savedUser.setEmail(email);
        storage.put(id, savedUser);
        emails.add(email);
        return savedUser;
    }

    @Override
    public void delUser(Long id) {
        if (storage.get(id) != null) {
            emails.remove(storage.get(id).getEmail());
            storage.remove(id);
        }
    }
}
