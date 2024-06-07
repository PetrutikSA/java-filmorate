package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int lastUserId;

    @Override
    public User createUser(User user) {
        user.setId(++lastUserId);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Created user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Attempt to update user with unknown id={}:\n{}", user.getId(), user);
            throw new ValidationException("Пользователя с таким ID не зарегестрировано");
        }
        users.put(user.getId(), user);
        log.info("Updated user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
