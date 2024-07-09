package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.RatingDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final FilmService filmService;

    @GetMapping
    public List<RatingDto> getRatings() {
        return filmService.getRatings();
    }

    @GetMapping("/{ratingId}")
    public RatingDto getRatingById(@PathVariable Integer ratingId) {
        return filmService.getRatingById(ratingId);
    }
}
