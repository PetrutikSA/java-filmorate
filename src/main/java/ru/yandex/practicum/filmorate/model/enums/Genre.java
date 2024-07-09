package ru.yandex.practicum.filmorate.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.BadRequestException;

import java.util.Objects;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    @Getter
    @JsonProperty
    private final Integer id;
    @JsonProperty
    private final String name;

    @JsonCreator
    public static Genre forValues(@JsonProperty("id") Integer id) {
        for (Genre genre : Genre.values()) {
            if (Objects.equals(genre.id, id)) {
                return genre;
            }
        }
        throw new BadRequestException(Genre.class);
    }
}
