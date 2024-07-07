package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film filmCreatedRequestToFilm(FilmCreateRequest filmCreateRequest) {
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

    public static Film updateFilmFields(Film film, FilmUpdateRequest filmUpdateRequest) {
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

    public static FilmDto userToUserDto(Film film) {
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
