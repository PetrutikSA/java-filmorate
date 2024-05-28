package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody @Validated(Create.class) @NonNull Film film) {
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Created film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated(Update.class) @NonNull Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Attempt to update film with unknown id={}:\n{}", film.getId(), film);
            throw new ValidationException("Фильма с таким ID не зарегистрировано");
        }
        films.put(film.getId(), film);
        log.info("Updated film with id={}:\n{}", film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private Integer getNextFilmId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}