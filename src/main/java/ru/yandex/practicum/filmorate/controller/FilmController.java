package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@RequestBody @Validated(Create.class) @NonNull Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated(Update.class) @NonNull Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film userPostLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userPostLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film userDeleteLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userDeleteLikeToFilm(filmId, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getTopLikedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopLikedFilms(count);
    }
}