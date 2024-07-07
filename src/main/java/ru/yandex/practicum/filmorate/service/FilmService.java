package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;

import java.util.List;

public interface FilmService {
    FilmDto addFilm(FilmCreateRequest film);

    FilmDto updateFilm(FilmUpdateRequest film);

    List<FilmDto> getAllFilms();

    FilmDto userPostLikeToFilm(Integer filmId, Integer userId);

    FilmDto userDeleteLikeToFilm(Integer filmId, Integer userId);

    List<FilmDto> getTopLikedFilms(Integer count);

    FilmDto getFilmById(Integer filmId);

    List<Genre> getGenres();

    Genre getGenreById(Integer genreId);

    List<Rating> getRatings();

    Rating getRatingById(Integer ratingId);
}
