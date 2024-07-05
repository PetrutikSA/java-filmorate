package ru.yandex.practicum.filmorate.storage.inmemorry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
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
        user.setFriendsId(new HashMap<>());
        users.put(user.getId(), user);
        log.info("Created user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Updated user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }
}
