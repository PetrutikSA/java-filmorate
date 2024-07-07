package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        ifUsersLikesSetNullInitialize(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkAndGetFilmById(film.getId());
        ifUsersLikesSetNullInitialize(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film userPostLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.checkAndGetUserById(userId);
        film = filmStorage.userPostLikeToFilm(film, userId);
        log.info("Users with id={} liked film with id={}", userId, filmId);
        return film;
    }

    @Override
    public Film userDeleteLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.checkAndGetUserById(userId);
        film = filmStorage.userDeleteLikeToFilm(film, userId);
        log.info("Users with id={} unliked film with id={}", userId, filmId);
        return film;
    }

    @Override
    public List<Film> getTopLikedFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getUsersIdPostedLikes().size(), film1.getUsersIdPostedLikes().size()))
                .limit(count)
                .toList();
    }

    public Film checkAndGetFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(id);
        } else return film;
    }

    private void ifUsersLikesSetNullInitialize(Film film) {
        if (film.getUsersIdPostedLikes() == null) {
            film.setUsersIdPostedLikes(new HashSet<>());
        }
    }

    @Override
    public List<Genre> getGenres() {
        return List.of(Genre.values());
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        if (genreId < 1 || genreId > Genre.values().length) {
            throw new GenreNotFoundException(genreId);
        }
        return Genre.forValues(genreId);
    }

    @Override
    public List<Rating> getRatings() {
        return List.of(Rating.values());
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        if (ratingId < 1 || ratingId > Rating.values().length) {
            throw new RatingNotFoundException(ratingId);
        }
        return Rating.forValues(ratingId);
    }
}
