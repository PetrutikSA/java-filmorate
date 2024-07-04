package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationDeserializer;
import ru.yandex.practicum.filmorate.model.adapter.JsonDurationSerializer;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;
import ru.yandex.practicum.filmorate.model.validator.annotation.After;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Min(value = 1, groups = {Update.class})
    private Integer id;
    @NotBlank(groups = {Create.class, Update.class}, message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, groups = {Create.class, Update.class},
            message = "Описание фильма не должно быть более 200 символов")
    private String description;
    @After(value = "1895-12-28", groups = {Create.class, Update.class},
            message = "Дата релиза должна быть не раньше 1895-12-28")
    private LocalDate releaseDate;
    @DurationMin(groups = {Create.class, Update.class},
            message = "Продолжительность фильма должна быть положительным числом")
    @JsonSerialize(using = JsonDurationSerializer.class)
    @JsonDeserialize(using = JsonDurationDeserializer.class)
    private Duration duration;
    private Set<Integer> usersIdPostedLikes;
    private Set<Genre> genre;
    private Rating rating;
}
