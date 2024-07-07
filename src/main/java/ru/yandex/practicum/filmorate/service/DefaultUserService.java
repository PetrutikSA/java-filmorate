package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {
    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        ifNameEmptyFillWithLogin(user);
        ifFriendsSetNullInitialize(user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        checkAndGetUserById(user.getId());
        ifNameEmptyFillWithLogin(user);
        ifFriendsSetNullInitialize(user);
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
        Map<Integer,FriendshipStatus> friendsIDsOfUser = user.getFriendsId();
        Map<Integer, FriendshipStatus> friendsIDsOfFriend = friend.getFriendsId();
        if (friendsIDsOfUser.containsKey(friendId)) {
            log.warn("User with id={} already has friend with id={}", userId, friendId);
        } else if (friendsIDsOfFriend.containsKey(userId)) {
            userStorage.deleteFriend(friend, userId, FriendshipStatus.REQUESTED);
            user = userStorage.addFriend(user, friendId, FriendshipStatus.APPROVED);
            userStorage.addFriend(friend, userId, FriendshipStatus.APPROVED);
            log.info("Users with id={} and id={} become friends", userId, friendId);
        } else {
            user = userStorage.addFriend(user, friendId, FriendshipStatus.REQUESTED);
            log.info("Users with id={} requested to become friends with user id={}", userId, friendId);
        }
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = checkAndGetUserById(userId);
        User friend = checkAndGetUserById(friendId);
        Map<Integer,FriendshipStatus> friendsIDsOfUser = user.getFriendsId();
        Map<Integer, FriendshipStatus> friendsIDsOfFriend = friend.getFriendsId();
        if (!friendsIDsOfUser.containsKey(friendId)) {
            log.warn("User with id={} has no friend with id={}", userId, friendId);
        } else if (friendsIDsOfUser.get(friendId) == FriendshipStatus.REQUESTED) {
            user = userStorage.deleteFriend(user, friendId, FriendshipStatus.REQUESTED);
            log.info("Users with id={} no longer request friends with user with id={}", userId, friendId);
        } else {
            user = userStorage.deleteFriend(user, friendId, FriendshipStatus.APPROVED);
            friend = userStorage.deleteFriend(friend, userId, FriendshipStatus.APPROVED);
            userStorage.addFriend(friend, userId, FriendshipStatus.REQUESTED);
            log.info("Users with id={} no longer friend with user with id={} ", userId, friendId);
            log.info("Users with id={} now requested to become friends with user id={}", friendId, userId);
        }
        return user;
    }

    @Override
    public List<User> getFriendsList(Integer userId) { //return atso not approved friends
        User user = checkAndGetUserById(userId);
        return user.getFriendsId().keySet().stream()
                .map(this::checkAndGetUserById)
                .toList();
    }

    @Override
    public List<User> getCommonFriendListWithOtherUser(Integer userId, Integer otherUserId) {
        User user = checkAndGetUserById(userId);
        User otherUser = checkAndGetUserById(otherUserId);
        Set<Integer> userFriendsList = user.getFriendsId().keySet();
        userFriendsList.retainAll(otherUser.getFriendsId().keySet());
        return userFriendsList.stream()
                .map(this::checkAndGetUserById)
                .toList();
    }

    private void ifNameEmptyFillWithLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void ifFriendsSetNullInitialize(User user) {
        if (user.getFriendsId() == null) {
            user.setFriendsId(new HashMap<>());
        }
    }

    public User checkAndGetUserById(Integer id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        } else return user;
    }
}
