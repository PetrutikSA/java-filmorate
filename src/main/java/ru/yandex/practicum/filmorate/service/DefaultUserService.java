package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    /* Добавление в друзья,
    удаление из друзей,
    вывод списка общих друзей.
    То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     */
    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        ifNameEmptyFillWithLogin(user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        ifNameEmptyFillWithLogin(user);
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        return null;
    }

    private void ifNameEmptyFillWithLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
