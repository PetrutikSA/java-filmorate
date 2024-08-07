package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationDeserializer;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationSerializer;
import ru.yandex.practicum.filmorate.model.validator.annotation.After;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmUpdateRequest {
    @Min(value = 1)
    private Integer id;
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
    private LinkedHashSet<GenreDto> genres;
    private RatingDto mpa;

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasDuration() {
        return !(duration == null);
    }

    public boolean hasGenres() {
        return !(genres == null || genres.isEmpty());
    }

    public boolean hasRating() {
        return !(mpa == null);
    }

}
