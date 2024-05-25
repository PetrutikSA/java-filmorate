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

import java.time.LocalDate;
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
        filmValidationControl(film);
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
        filmValidationControl(film);
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

    private void filmValidationControl(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Attempt to use film with empty name id={}:\n{}", film.getId(), film);
            throw new ValidationException("Название фильма не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Attempt to use film with description length more than 200 id={}:\n{}", film.getId(), film);
            throw new ValidationException("Описание фильма не должно быть более 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Attempt to use film with release date early than 1895-12-28 id={}:\n{}", film.getId(), film);
            throw new ValidationException("Дата релиза должна быть не раньше 1895-12-28");
        } else if (film.getDuration().isNegative()) {
            log.warn("Attempt to use film with release negative Duration id={}:\n{}", film.getId(), film);
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}