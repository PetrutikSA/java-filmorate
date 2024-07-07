package ru.yandex.practicum.filmorate.exception;

public class NotCorrectRatingException extends RuntimeException{
    public NotCorrectRatingException() {
        super("Указан некорректный рейтинг");
    }
}
