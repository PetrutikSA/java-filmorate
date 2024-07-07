package ru.yandex.practicum.filmorate.exception;

public class NotCorrectGenreException extends RuntimeException{
    public NotCorrectGenreException() {
        super("Указан некорректный жанр");
    }
}
