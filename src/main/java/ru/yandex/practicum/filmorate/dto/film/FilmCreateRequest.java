package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationDeserializer;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationSerializer;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.model.validator.annotation.After;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmCreateRequest {
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно быть более 200 символов")
    private String description;
    @After(value = "1895-12-28", message = "Дата релиза должна быть не раньше 1895-12-28")
    private LocalDate releaseDate;
    @DurationMin(message = "Продолжительность фильма должна быть положительным числом")
    @JsonSerialize(using = JsonDurationSerializer.class)
    @JsonDeserialize(using = JsonDurationDeserializer.class)
    private Duration duration;
    private LinkedHashSet<Genre> genres;
    private Rating mpa;

}
