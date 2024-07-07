package ru.yandex.practicum.filmorate.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Rating {
    G (1, "у фильма нет возрастных ограничений,"),
    PG (2, "детям рекомендуется смотреть фильм с родителями,"),
    PG13 (3, "детям до 13 лет просмотр не желателен,"),
    R (4, "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC17(5, "лицам до 18 лет просмотр запрещён");

    @JsonProperty("id")
    private final Integer id;
    @JsonProperty("description")
    private final String description;

    @JsonCreator
    public static Rating forValues (@JsonProperty("id") Integer id) {
        for (Rating rating : Rating.values()) {
            if (Double.compare(rating.id, id) == 0) {
                return rating;
            }
        }
        return null;
    }
}
