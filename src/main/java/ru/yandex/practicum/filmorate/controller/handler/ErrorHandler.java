package ru.yandex.practicum.filmorate.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotCorrectGenreException;
import ru.yandex.practicum.filmorate.exception.NotCorrectRatingException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
        log.warn("Validation exception: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException exception) {
        log.warn("Requested non existed User with id={}", exception.getUserId());
        return new ErrorResponse(String.format("Пользователя с ID=%d не зарегистрировано", exception.getUserId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(FilmNotFoundException exception) {
        log.warn("Requested non existed Film with id={}", exception.getFilmId());
        return new ErrorResponse(String.format("Фильма с ID=%d не зарегистрировано", exception.getFilmId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotCorrectGenreException(NotCorrectGenreException exception) {
        log.warn("Not correct genre got from client");
        return new ErrorResponse(String.format(exception.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotCorrectRatingException(NotCorrectRatingException exception) {
        log.warn("Not correct rating got from client");
        return new ErrorResponse(String.format(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotCorrectRatingAndGenreException
            (HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause().getCause();
        if (cause instanceof NotCorrectRatingException) {
            log.warn("Not correct rating got from client");
            ErrorResponse errorResponse = new ErrorResponse(cause.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (cause instanceof NotCorrectGenreException) {
            log.warn("Not correct genre got from client");
            ErrorResponse errorResponse = new ErrorResponse(cause.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(handleRuntimeException(exception), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(Exception exception) {
        log.warn("Unexpected exception", exception);
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFoundException(GenreNotFoundException exception) {
        log.warn("Requested non existed Genre with id={}", exception.getGenreId());
        return new ErrorResponse(String.format("Жанра с ID=%d не зарегистрировано", exception.getGenreId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRatingNotFoundException(RatingNotFoundException exception) {
        log.warn("Requested non existed Rating with id={}", exception.getRating());
        return new ErrorResponse(String.format("Рейтинга с ID=%d не зарегистрировано", exception.getRating()));
    }
}
