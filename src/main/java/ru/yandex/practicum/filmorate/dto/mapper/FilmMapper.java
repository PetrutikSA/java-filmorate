package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;

@UtilityClass
public final class FilmMapper {
    public Film filmCreatedRequestToFilm(FilmCreateRequest filmCreateRequest) {
        Film film = Film.builder()
                .name(filmCreateRequest.getName())
                .description(filmCreateRequest.getDescription())
                .releaseDate(filmCreateRequest.getReleaseDate())
                .duration(filmCreateRequest.getDuration())
                .genres(filmCreateRequest.getGenres())
                .mpa(filmCreateRequest.getMpa())
                .build();
        film.setUsersIdPostedLikes(new HashSet<>());
        return film;
    }

    public Film updateFilmFields(Film film, FilmUpdateRequest filmUpdateRequest) {
        film.setName(filmUpdateRequest.getName());

        if (filmUpdateRequest.hasDescription()) {
            film.setDescription(filmUpdateRequest.getDescription());
        }
        if (filmUpdateRequest.hasReleaseDate()) {
            film.setReleaseDate(filmUpdateRequest.getReleaseDate());
        }
        if (filmUpdateRequest.hasDuration()) {
            film.setDuration(filmUpdateRequest.getDuration());
        }
        if (filmUpdateRequest.hasGenres()) {
            film.setGenres(filmUpdateRequest.getGenres());
        }
        if (filmUpdateRequest.hasRating()) {
            film.setMpa(filmUpdateRequest.getMpa());
        }
        return film;
    }

    public FilmDto filmToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpa())
                .usersIdPostedLikes(film.getUsersIdPostedLikes())
                .build();
    }

}
