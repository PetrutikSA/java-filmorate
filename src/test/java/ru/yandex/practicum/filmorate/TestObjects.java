package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.model.enums.Rating;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;

public class TestObjects {
    public UserCreateRequest user = new UserCreateRequest("name@mail.ru", "login", "name",LocalDate.of(2000, 10, 15));
    public UserUpdateRequest updatedUser = new UserUpdateRequest(1, "organization@mail.ru", "newLogin", "Surname",
            LocalDate.of(1989, 7, 27));
    public UserCreateRequest user2 = new UserCreateRequest("organization@mail.ru", "newLogin", "Surname",
            LocalDate.of(1989, 7, 27));
    public FilmCreateRequest film = new FilmCreateRequest("name1", "Description1",
            LocalDate.of(2000, 10, 15), Duration.ofMinutes(120), new LinkedHashSet<>(), Rating.PG);
    public FilmUpdateRequest updatedFilm = new FilmUpdateRequest(1, "updatedName", "UpdatedDescription",
                                        LocalDate.of(2010, 1, 1), Duration.ofMinutes(180), new LinkedHashSet<>(), Rating.PG);
    public FilmCreateRequest film2 = new FilmCreateRequest("name2", "description2",
            LocalDate.of(2010, 1, 1), Duration.ofMinutes(180), new LinkedHashSet<>(), Rating.PG);

}
