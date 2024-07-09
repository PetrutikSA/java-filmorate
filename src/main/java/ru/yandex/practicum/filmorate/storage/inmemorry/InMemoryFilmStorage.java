package ru.yandex.practicum.filmorate.storage.inmemorry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Genre> genres;
    private final Map<Integer, Rating> ratings;
    private int lastFilmId;

    @Autowired
    public InMemoryFilmStorage() {
        genres = fillGenres();
        ratings = fillRatings();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++lastFilmId);
        if (film.getGenres() == null) {
            film.setGenres(new LinkedHashSet<>());
        }
        films.put(film.getId(), film);
        log.info("Created film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Updated film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public Film userPostLikeToFilm(Film film, Integer userId) {
        film.getUsersIdPostedLikes().add(userId);
        return film;
    }

    @Override
    public Film userDeleteLikeToFilm(Film film, Integer userId) {
        film.getUsersIdPostedLikes().remove(userId);
        return film;
    }

    @Override
    public List<Genre> getGenres() {
        return genres.values().stream().toList();
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        return genres.get(genreId);
    }

    @Override
    public List<Rating> getRatings() {
        return ratings.values().stream().toList();
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        return ratings.get(ratingId);
    }

    private Map<Integer, Rating> fillRatings() {
        Map<Integer, Rating> ratingObject = new HashMap<>();
        ratingObject.put(1, new Rating(1, "G"));
        ratingObject.put(2, new Rating(2, "PG"));
        ratingObject.put(3, new Rating(3, "PG-13"));
        ratingObject.put(4, new Rating(4, "R"));
        ratingObject.put(5, new Rating(5, "NC-17"));
        return ratingObject;
    }

    private Map<Integer, Genre> fillGenres() {
        Map<Integer, Genre> genresObjects = new HashMap<>();
        genresObjects.put(1, new Genre(1, "Комедия"));
        genresObjects.put(2, new Genre(2, "Драма"));
        genresObjects.put(3, new Genre(3, "Мультфильм"));
        genresObjects.put(4, new Genre(4, "Триллер"));
        genresObjects.put(5, new Genre(5, "Документальный"));
        genresObjects.put(6, new Genre(6, "Боевик"));
        return genresObjects;
    }
}
