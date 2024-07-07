package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateRequest user);

    UserDto updateUser(UserUpdateRequest user);

    List<UserDto> getAllUsers();

    UserDto addFriend(Integer userId, Integer friendId);

    UserDto deleteFriend(Integer userId, Integer friendId);

    List<UserDto> getFriendsList(Integer userId);

    List<UserDto> getCommonFriendListWithOtherUser(Integer userId, Integer otherUserId);

    UserDto getById(Integer id);
}
