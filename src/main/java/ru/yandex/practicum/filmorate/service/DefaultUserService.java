package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        ifNameEmptyFillWithLogin(user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            log.warn("Attempt to update user with unknown id={}:\n{}", user.getId(), user);
            throw new ValidationException("Пользователя с таким ID не зарегистрировано");
        }
        ifNameEmptyFillWithLogin(user);
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = checkAndGetUserById(userId);
        User friend = checkAndGetUserById(friendId);
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(userId);
        log.info(String.format("Users with id=%d and id=%s become friends", userId, friendId));
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = checkAndGetUserById(userId);
        User friend = checkAndGetUserById(friendId);
        user.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(userId);
        log.info(String.format("Users with id=%d and id=%s no longer friends", userId, friendId));
        return null;
    }

    @Override
    public List<User> getFriendsList(Integer userId) {
        User user = checkAndGetUserById(userId);
        return user.getFriendsId().stream()
                .map(this::checkAndGetUserById)
                .toList();
    }

    @Override
    public List<User> getCommonFriendListWithOtherUser(Integer userId, Integer otherUserId) {
        User user = checkAndGetUserById(userId);
        User otherUser = checkAndGetUserById(otherUserId);
        Set<Integer> userFriendsList = new HashSet<>(user.getFriendsId());
        userFriendsList.retainAll(otherUser.getFriendsId());
        return userFriendsList.stream()
                .map(this::checkAndGetUserById)
                .toList();
    }

    private void ifNameEmptyFillWithLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User checkAndGetUserById(Integer id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.warn(String.format("Requested non existed User with id=%d", id));
            throw new UserNotFoundException(id);
        } else return user;
    }
}
