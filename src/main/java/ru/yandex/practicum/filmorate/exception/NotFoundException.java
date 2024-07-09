package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Integer entityId;
    private final Class<?> entity;

    public NotFoundException(Integer entityId, Class<?> entity) {
        super();
        this.entityId = entityId;
        this.entity = entity;
    }
}
