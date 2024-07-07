package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film addFilm(@RequestBody @Validated(Create.class) @NonNull Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody @Validated(Update.class) @NonNull Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{filmId}")
    public Film getById(@PathVariable Integer filmId) {
        return filmService.checkAndGetFilmById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public Film userPostLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userPostLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public Film userDeleteLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userDeleteLikeToFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopLikedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopLikedFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{genre_id}")
    public Genre getGenreById(@PathVariable Integer genre_id) {
        return filmService.getGenreById(genre_id);
    }

    @GetMapping("/mpa")
    public List<Rating> getRatings() {
        return filmService.getRatings();
    }

    @GetMapping("/mpa/{rating_id}")
    public Rating getRatingById(@PathVariable Integer rating_id) {
        return filmService.getRatingById(rating_id);
    }
}