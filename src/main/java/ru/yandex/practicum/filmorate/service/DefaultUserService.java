package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mapper.UserMapper;
import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    public UserDto createUser(UserCreateRequest userCreateRequest) {
        User user = UserMapper.userCreatedRequestToUser(userCreateRequest);
        ifNameEmptyFillWithLogin(user);
        ifFriendsSetNullInitialize(user);
        user = userStorage.createUser(user);
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateRequest userUpdateRequest) {
        User user = checkAndGetUserById(userUpdateRequest.getId());
        user = UserMapper.updateUserFields(user, userUpdateRequest);
        return UserMapper.userToUserDto(userStorage.updateUser(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::userToUserDto)
                .toList();
    }

    @Override
    public UserDto getById(Integer id) {
        return UserMapper.userToUserDto(checkAndGetUserById(id));
    }

    @Override
    public UserDto addFriend(Integer userId, Integer friendId) {
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
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto deleteFriend(Integer userId, Integer friendId) {
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
        return UserMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> getFriendsList(Integer userId) { //return atso not approved friends
        User user = checkAndGetUserById(userId);
        return user.getFriendsId().keySet().stream()
                .map(this::checkAndGetUserById)
                .map(UserMapper::userToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> getCommonFriendListWithOtherUser(Integer userId, Integer otherUserId) {
        User user = checkAndGetUserById(userId);
        User otherUser = checkAndGetUserById(otherUserId);
        Set<Integer> userFriendsList = user.getFriendsId().keySet();
        userFriendsList.retainAll(otherUser.getFriendsId().keySet());
        return userFriendsList.stream()
                .map(this::checkAndGetUserById)
                .map(UserMapper::userToUserDto)
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

    private User checkAndGetUserById(Integer id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException(id, User.class);
        } else return user;
    }
}
