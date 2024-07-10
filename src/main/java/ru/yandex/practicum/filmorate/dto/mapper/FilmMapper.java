package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.LinkedHashSet;

@UtilityClass
public final class FilmMapper {
    public Film filmCreatedRequestToFilm(FilmCreateRequest filmCreateRequest) {
        Film film = Film.builder()
                .name(filmCreateRequest.getName())
                .description(filmCreateRequest.getDescription())
                .releaseDate(filmCreateRequest.getReleaseDate())
                .duration(filmCreateRequest.getDuration())
                .mpa(RatingMapper.ratingDtoToRating(filmCreateRequest.getMpa()))
                .build();
        if (filmCreateRequest.getGenres() != null) {
            film.setGenres(new LinkedHashSet<>(filmCreateRequest.getGenres().stream()
                    .map(GenreMapper::genreDtoToGenre).toList()));
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
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
            film.setGenres(new LinkedHashSet<>(filmUpdateRequest.getGenres().stream()
                    .map(GenreMapper::genreDtoToGenre).toList()));
        }
        if (filmUpdateRequest.hasRating()) {
            film.setMpa(RatingMapper.ratingDtoToRating(filmUpdateRequest.getMpa()));
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
                .genres(new LinkedHashSet<>(film.getGenres().stream()
                        .map(GenreMapper::genreToGenreDto).toList()))
                .mpa(RatingMapper.ratingToRatingDto(film.getMpa()))
                .usersIdPostedLikes(film.getUsersIdPostedLikes())
                .build();
    }

}
