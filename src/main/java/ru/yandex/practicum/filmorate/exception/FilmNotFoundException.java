package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class FilmNotFoundException extends RuntimeException {
    private final Integer filmId;

    public FilmNotFoundException(String message, Integer filmId) {
        super(message);
        this.filmId = filmId;
    }
}
