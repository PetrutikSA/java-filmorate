package ru.yandex.practicum.filmorate.controller.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception exception) {
        log.warn("Validation exception: {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(NotFoundException exception) {
        log.warn("Requested non existed {} with id={}", exception.getEntity().getName(),
                exception.getEntityId(), exception);
        return new ErrorResponse(String.format("Фильма с ID=%d не зарегистрировано", exception.getEntityId()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotCorrectGenreException(BadRequestException exception) {
        log.warn("Not correct {} got from client", exception.getEntity().getName(), exception);
        return new ErrorResponse(String.format(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotCorrectRatingAndGenreException(
            HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause().getCause();
        if (cause instanceof BadRequestException) {
            log.warn("Not correct {} got from client", ((BadRequestException) cause).getEntity().getName(), exception);
            ErrorResponse errorResponse = new ErrorResponse(cause.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }  else {
            return new ResponseEntity<>(handleRuntimeException(exception), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(Exception exception) {
        log.warn("Unexpected exception", exception);
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }
}
