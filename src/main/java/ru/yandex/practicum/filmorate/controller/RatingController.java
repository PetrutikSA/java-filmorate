package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final FilmService filmService;

    @GetMapping
    public List<Rating> getRatings() {
        return filmService.getRatings();
    }

    @GetMapping("/{ratingId}")
    public Rating getRatingById(@PathVariable Integer ratingId) {
        return filmService.getRatingById(ratingId);
    }
}
