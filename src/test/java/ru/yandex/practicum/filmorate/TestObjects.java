package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;

import java.time.LocalDate;

public class TestObjects {
    public UserCreateRequest user = new UserCreateRequest("name@mail.ru", "login", "name",LocalDate.of(2000, 10, 15));
    public UserUpdateRequest updatedUser = new UserUpdateRequest(1, "organization@mail.ru", "newLogin", "Surname",
            LocalDate.of(1989, 7, 27));
    public UserCreateRequest user2 = new UserCreateRequest("organization@mail.ru", "newLogin", "Surname",
            LocalDate.of(1989, 7, 27));
}
