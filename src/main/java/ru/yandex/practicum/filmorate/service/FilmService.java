package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.dto.film.GenreDto;
import ru.yandex.practicum.filmorate.dto.film.RatingDto;

import java.util.List;

public interface FilmService {
    FilmDto addFilm(FilmCreateRequest film);

    FilmDto updateFilm(FilmUpdateRequest film);

    List<FilmDto> getAllFilms();

    FilmDto userPostLikeToFilm(Integer filmId, Integer userId);

    FilmDto userDeleteLikeToFilm(Integer filmId, Integer userId);

    List<FilmDto> getTopLikedFilms(Integer count);

    FilmDto getFilmById(Integer filmId);

    List<GenreDto> getGenres();

    GenreDto getGenreById(Integer genreId);

    List<RatingDto> getRatings();

    RatingDto getRatingById(Integer ratingId);
}
