package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class RatingDto {
    private final Integer id;
    private final String name;
}
