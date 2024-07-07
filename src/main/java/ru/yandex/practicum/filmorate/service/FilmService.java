package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film userPostLikeToFilm(Integer filmId, Integer userId);

    Film userDeleteLikeToFilm(Integer filmId, Integer userId);

    List<Film> getTopLikedFilms(Integer count);

    Film checkAndGetFilmById(Integer id);

    List<Genre> getGenres();

    Genre getGenreById(Integer genreId);

    List<Rating> getRatings();

    Rating getRatingById(Integer ratingId);
}
