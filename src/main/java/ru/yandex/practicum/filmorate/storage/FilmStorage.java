package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Integer id);

    Film userPostLikeToFilm(Film film, Integer userId);

    Film userDeleteLikeToFilm(Film film, Integer userId);
}
