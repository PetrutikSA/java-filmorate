package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dto.user.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.FilmUpdateRequest;
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

    public FilmDto addFilm(FilmCreateRequest filmCreateRequest) {
        Film film = FilmMapper.filmCreatedRequestToFilm(filmCreateRequest);
        return FilmMapper.filmToFilmDto(filmStorage.addFilm(film));
    }

    public FilmDto updateFilm(FilmUpdateRequest filmUpdateRequest) {
        Film film = checkAndGetFilmById(filmUpdateRequest.getId());
        film = FilmMapper.updateFilmFields(film, filmUpdateRequest);
        return FilmMapper.filmToFilmDto(filmStorage.updateFilm(film));
    }

    public List<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(FilmMapper::filmToFilmDto)
                .toList();
    }

    public FilmDto getFilmById(Integer filmId) {
        return FilmMapper.filmToFilmDto(checkAndGetFilmById(filmId));
    }

    @Override
    public FilmDto userPostLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.getById(userId);
        film = filmStorage.userPostLikeToFilm(film, userId);
        log.info("Users with id={} liked film with id={}", userId, filmId);
        return FilmMapper.filmToFilmDto(film);
    }

    @Override
    public FilmDto userDeleteLikeToFilm(Integer filmId, Integer userId) {
        Film film = checkAndGetFilmById(filmId);
        userService.getById(userId);
        film = filmStorage.userDeleteLikeToFilm(film, userId);
        log.info("Users with id={} unliked film with id={}", userId, filmId);
        return FilmMapper.filmToFilmDto(film);
    }

    @Override
    public List<FilmDto> getTopLikedFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getUsersIdPostedLikes().size(), film1.getUsersIdPostedLikes().size()))
                .limit(count)
                .map(FilmMapper::filmToFilmDto)
                .toList();
    }

    private Film checkAndGetFilmById(Integer id) {
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
