package ru.yandex.practicum.filmorate.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.BadRequestException;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Rating {
    G(1, "G", "у фильма нет возрастных ограничений"),
    PG(2, "PG", "детям рекомендуется смотреть фильм с родителями"),
    PG13(3, "PG-13", "детям до 13 лет просмотр не желателен"),
    R(4, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC17(5, "NC-17", "лицам до 18 лет просмотр запрещён");

    @Getter
    @JsonProperty("id")
    private final Integer id;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("description")
    private final String description;

    @JsonCreator
    public static Rating forValues(@JsonProperty("id") Integer id) {
        for (Rating rating : Rating.values()) {
            if (rating.id.equals(id)) {
                return rating;
            }
        }
        throw new BadRequestException(Rating.class);
    }
}
