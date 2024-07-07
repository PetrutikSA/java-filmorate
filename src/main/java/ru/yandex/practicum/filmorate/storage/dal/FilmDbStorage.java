package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

public class FilmDbStorage extends BaseDbStorage<Film>  implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE user_id = ?";
    private static final String FIND_ALL_GENRES_QUERY = "SELECT film_id, genre_id, FROM films_genres";
    private static final String FIND_BY_FILM_ID_GENRES_QUERY = "SELECT genre_id, FROM films_genres WHERE film_id = ?";


    @Override
    public Film getFilmById(Integer id) {

        return null;
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }
}
