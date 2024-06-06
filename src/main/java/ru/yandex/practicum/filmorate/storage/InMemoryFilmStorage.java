package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int lastFilmId;

    @Override
    public Film addFilm(Film film) {
        film.setId(++lastFilmId);
        films.put(film.getId(), film);
        log.info("Created film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Attempt to update film with unknown id={}:\n{}", film.getId(), film);
            throw new ValidationException("Фильма с таким ID не зарегистрировано");
        }
        films.put(film.getId(), film);
        log.info("Updated film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
