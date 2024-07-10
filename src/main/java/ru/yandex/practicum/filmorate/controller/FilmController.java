package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public FilmDto addFilm(@RequestBody @Valid FilmCreateRequest film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody @Valid FilmUpdateRequest film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto getById(@PathVariable Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto userPostLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userPostLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto userDeleteLikeToFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.userDeleteLikeToFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getTopLikedFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getTopLikedFilms(count);
    }
}