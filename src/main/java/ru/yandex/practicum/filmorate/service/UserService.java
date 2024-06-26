package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer userId, Integer friendId);

    List<User> getFriendsList(Integer userId);

    List<User> getCommonFriendListWithOtherUser(Integer userId, Integer otherUserId);

    User checkAndGetUserById(Integer id);
}
