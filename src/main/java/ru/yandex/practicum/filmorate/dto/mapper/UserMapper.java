package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User userCreatedRequestToUser(UserCreateRequest userCreateRequest) {
        User user = User.builder()
                .email(userCreateRequest.getEmail())
                .login(userCreateRequest.getLogin())
                .name(userCreateRequest.getName())
                .birthday(userCreateRequest.getBirthday())
                .build();
        user.setFriendsId(new HashMap<>());
        return user;
    }

    public static User updateUserFields(User user, UserUpdateRequest userUpdateRequest) {
        user.setEmail(userUpdateRequest.getEmail());
        if (userUpdateRequest.hasName()) {
            user.setName(userUpdateRequest.getName());
        }
        if (userUpdateRequest.hasLogin()) {
            user.setLogin(userUpdateRequest.getLogin());
        }
        if (userUpdateRequest.hasBirthday()) {
            user.setBirthday(userUpdateRequest.getBirthday());
        }
        return user;
    }

    public static UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friendsId(user.getFriendsId())
                .build();
    }
}
