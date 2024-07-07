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
import ru.yandex.practicum.filmorate.dto.user.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.FilmUpdateRequest;
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
    public FilmDto addFilm(@RequestBody @Validated(Create.class) @NonNull FilmCreateRequest film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody @Validated(Update.class) @NonNull FilmUpdateRequest film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{filmId}")
    public FilmDto getById(@PathVariable Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public FilmDto userPostLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userPostLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public FilmDto userDeleteLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userDeleteLikeToFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<FilmDto> getTopLikedFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopLikedFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable Integer genreId) {
        return filmService.getGenreById(genreId);
    }

    @GetMapping("/mpa")
    public List<Rating> getRatings() {
        return filmService.getRatings();
    }

    @GetMapping("/mpa/{ratingId}")
    public Rating getRatingById(@PathVariable Integer ratingId) {
        return filmService.getRatingById(ratingId);
    }
}