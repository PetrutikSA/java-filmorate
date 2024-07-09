package ru.yandex.practicum.filmorate.storage.inmemorry;

import lombok.extern.slf4j.Slf4j;
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
    private final Map<Integer, Rating> ratings = new HashMap<>();
    private int lastFilmId;

    public InMemoryFilmStorage() {
        genres = fillGenres();
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
        /*
           G(1, "G", "у фильма нет возрастных ограничений"),
    PG(2, "PG", "детям рекомендуется смотреть фильм с родителями"),
    PG13(3, "PG-13", "детям до 13 лет просмотр не желателен"),
    R(4, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC17(5, "NC-17", "лицам до 18 лет просмотр запрещён");
        * */
        return null;
    }

    private Map<Integer, Genre> fillGenres() {
        Map<Integer, Genre> genresObjects = new HashMap<>();
        genres.put(1, new Genre(1, "Комедия"));
        genres.put(2, new Genre(2, "Драма"));
        genres.put(3, new Genre(3, "Мультфильм"));
        genres.put(4, new Genre(4, "Триллер"));
        genres.put(5, new Genre(5, "Документальный"));
        genres.put(6, new Genre(6, "Боевик"));
        return genresObjects;
    }
}
