package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.mapper.GenreExtractor;
import ru.yandex.practicum.filmorate.storage.dal.mapper.LikesExtractor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Primary
@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private final GenreExtractor genreExtractor;
    private final LikesExtractor likesExtractor;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
        genreExtractor = new GenreExtractor();
        likesExtractor = new LikesExtractor();
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String FIND_ALL_GENRES_QUERY = "SELECT film_id, genre_id FROM films_genres";
    private static final String FIND_BY_FILM_ID_GENRES_QUERY = "SELECT film_id, genre_id " +
            "FROM films_genres WHERE film_id = ?";
    private static final String FIND_ALL_LIKES_QUERY = "SELECT film_id, user_id FROM films_likes";
    private static final String FIND_BY_FILM_ID_LIKES_QUERY = "SELECT film_id, user_id " +
            "FROM films_likes WHERE film_id = ?";

    private static final String INSERT_QUERY = "INSERT INTO films " +
            "(name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO films_genres " +
            "(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String INSERT_FILM_LIKE_QUERY = "INSERT INTO films_likes " +
            "(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_FILM_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";

    @Override
    public Film getFilmById(Integer id) {
        Map<Integer, LinkedHashSet<Genre>> genresMap = jdbc.query(FIND_BY_FILM_ID_GENRES_QUERY, genreExtractor, id);
        Map<Integer, Set<Integer>> likesMap = jdbc.query(FIND_BY_FILM_ID_LIKES_QUERY, likesExtractor, id);
        try {
            Film film = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            if (film != null && genresMap != null && !genresMap.isEmpty()) {
                film.setGenres(genresMap.get(id));
            }
            if (film != null && likesMap != null && !likesMap.isEmpty()) {
                film.setUsersIdPostedLikes(likesMap.get(id));
            }
            return film;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Film> getAllFilms() {
        Map<Integer, LinkedHashSet<Genre>> genresMap = jdbc.query(FIND_ALL_GENRES_QUERY, genreExtractor);
        Map<Integer, Set<Integer>> likesMap = jdbc.query(FIND_ALL_LIKES_QUERY, likesExtractor);

        List<Film> films = jdbc.query(FIND_ALL_QUERY, mapper);
        for (Film film : films) {
            if (genresMap != null && !genresMap.isEmpty()) {
                Integer filmId = film.getId();

                if ((genresMap.containsKey(filmId))) {
                    film.setGenres(genresMap.get(filmId));
                } else {
                    film.setGenres(new LinkedHashSet<>());
                }

                if (likesMap != null && !likesMap.isEmpty() && likesMap.containsKey(filmId)) {
                    film.setUsersIdPostedLikes(likesMap.get(filmId));
                }
            }
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        Integer id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa().getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                insert(INSERT_FILM_GENRES_QUERY, id, genre.getId());
            }
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer id = film.getId();
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa().getId(),
                id);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            update(DELETE_FILM_GENRES_QUERY, id);
            for (Genre genre : film.getGenres()) {
                insert(INSERT_FILM_GENRES_QUERY, id, genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film userPostLikeToFilm(Film film, Integer userId) {
        insert(INSERT_FILM_LIKE_QUERY, film.getId(), userId);
        film.getUsersIdPostedLikes().add(userId);
        return film;
    }

    @Override
    public Film userDeleteLikeToFilm(Film film, Integer userId) {
        update(DELETE_FILM_LIKE_QUERY, film.getId(), userId);
        film.getUsersIdPostedLikes().remove(userId);
        return film;
    }
}
