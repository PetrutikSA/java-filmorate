package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    User deleteFriend(User user, Integer friendId, FriendshipStatus status);

    User addFriend(User user, Integer friendId, FriendshipStatus status);
}
