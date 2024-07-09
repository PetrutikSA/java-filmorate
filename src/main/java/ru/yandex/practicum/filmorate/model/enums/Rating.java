package ru.yandex.practicum.filmorate.model.enums;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rating {
    private final Integer id;
    private final String name;
}
