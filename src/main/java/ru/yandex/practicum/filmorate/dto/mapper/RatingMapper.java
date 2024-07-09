package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.film.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

@UtilityClass
public class RatingMapper {
    public Rating ratingDtoToRating(RatingDto ratingDto) {
        return Rating.builder()
                .id(ratingDto.getId())
                .name(ratingDto.getName())
                .build();
    }

    public RatingDto ratingToRatingDto(Rating rating) {
        return RatingDto.builder()
                .id(rating.getId())
                .name(rating.getName())
                .build();
    }
}
