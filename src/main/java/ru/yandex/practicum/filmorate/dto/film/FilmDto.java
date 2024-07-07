package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationDeserializer;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationSerializer;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    @JsonSerialize(using = JsonDurationSerializer.class)
    @JsonDeserialize(using = JsonDurationDeserializer.class)
    private Duration duration;
    private Set<Integer> usersIdPostedLikes;
    private LinkedHashSet<Genre> genres;
    private Rating mpa;
}
