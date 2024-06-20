package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.HashSet;
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
        if (friendsIDsOfFriend.containsKey(userId)) {
            friendsIDsOfFriend.put(userId, FriendshipStatus.APPROVED);
            friendsIDsOfUser.put(friendId, FriendshipStatus.APPROVED);
            log.info("Users with id={} and id={} become friends", userId, friendId);
            return user;
        } else {
            friendsIDsOfUser.put(friendId, FriendshipStatus.REQUESTED);
            log.info("Users with id={} requested to become friends with user id={}", userId, friendId);
            return user;
        }
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = checkAndGetUserById(userId);
        User friend = checkAndGetUserById(friendId);
        user.getFriendsId().remove(friendId);
        Map<Integer, FriendshipStatus> friendsIDsOfFriend = friend.getFriendsId();
        if (friendsIDsOfFriend.containsKey(userId)) {
            friendsIDsOfFriend.put(userId, FriendshipStatus.REQUESTED);
        }
        log.info("Users with id={} and id={} no longer friends", userId, friendId);
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
        Set<Integer> userFriendsList = getApprovedFriendsIds(user);
        userFriendsList.retainAll(getApprovedFriendsIds(otherUser));
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

    private Set<Integer> getApprovedFriendsIds(User user) {
        Map<Integer, FriendshipStatus> friendsIds = user.getFriendsId();
        Set<Integer> friendsIDsSet = new HashSet<>(friendsIds.keySet());
        for (Integer id: friendsIds.keySet()) {
            if (friendsIds.get(id) != FriendshipStatus.APPROVED) {
                friendsIDsSet.remove(id);
            }
        }
        return friendsIDsSet;
    }
}
