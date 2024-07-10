package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final Class<?> entity;

    public BadRequestException(Class<?> entity) {
        super(String.format("Указан некорректный %s", entity.getName()));
        this.entity = entity;
    }
}
