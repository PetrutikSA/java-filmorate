package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFilmService implements FilmService{
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkAndGetFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film userPostLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.checkAndGetUserById(userId);
        film.getUsersIdPostedLikes().add(userId);
        log.info(String.format("Users with id=%d liked film with id=%d", userId, filmId));
        return film;
    }

    @Override
    public Film userDeleteLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.checkAndGetUserById(userId);
        film.getUsersIdPostedLikes().remove(userId);
        log.info(String.format("Users with id=%d unliked film with id=%d", userId, filmId));
        return film;
    }

    @Override
    public List<Film> getTopLikedFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getUsersIdPostedLikes().size()))
                .limit(count)
                .toList();
    }

    public Film checkAndGetFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.warn(String.format("Requested non existed Film with id=%d", id));
            throw new FilmNotFoundException(String.format("Фильма с ID=%d не зарегистрировано", id), id);
        } else return film;
    }
}
