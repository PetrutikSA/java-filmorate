package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.DefaultFilmService;
import ru.yandex.practicum.filmorate.service.DefaultUserService;
import ru.yandex.practicum.filmorate.storage.inmemorry.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemorry.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTests {

    private FilmController filmController;
    private TestObjects testObjects;
    private FilmCreateRequest film;
    private FilmUpdateRequest updatedFilm;
    private final String notCorrectName = " ";
    private final String notCorrectDescription = "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
            "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. " +
            "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.";
    private final LocalDate notCorrectReleaseDate = LocalDate.of(1800, 10, 15);
    private final Duration notCorrectDuration = Duration.ofMinutes(-120);
    private final String returnNotCorrectFilm = "Фильм в базе данных не равен соответствует отправленному";
    private final String returnFilmsListNotCorrectSize = "Некорректный размер списка фильмов";
    private final String emptyNameValidationViolation = "Фильм без имени должен приводить к ошибке";
    private final String descriptionSizeValidationViolation = "Фильм с описанием более 200 символов должен приводить к ошибке";
    private final String releaseDateBorderValidationViolation = "Фильм отрицательной продолжительностью должен приводить к ошибке";
    private final String negativeDurationValidationViolation = "Фильм отрицательной продолжительностью должен приводить к ошибке";
    private Validator validator;


    @BeforeEach
    void beforeEach() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        testObjects = new TestObjects();
        filmController = new FilmController(new DefaultFilmService(new InMemoryFilmStorage(),
                new DefaultUserService(new InMemoryUserStorage())));
        film = testObjects.film;
        updatedFilm = testObjects.updatedFilm;
    }

    @Test
    void filmIsNullThrowException() {
        assertThrows(NullPointerException.class, () -> filmController.addFilm(null),
                "Пустой запрос должен приводить к ошибке");
    }

    @Test
    void correctFilmCreated() {
        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Корректный фильм не проходит валидацию");
        filmController.addFilm(film);
        List<FilmDto> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size(), returnFilmsListNotCorrectSize);
        FilmDto filmFromController = filmList.get(0);
        assertEquals(film.getName(), filmFromController.getName(), returnNotCorrectFilm);
    }

    @Test
    void createdEmptyNameFilmValidationsProvideError() {
        film.setName(notCorrectName);
        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), emptyNameValidationViolation);
    }

    @Test
    void createFilmDescriptionLengthMore200ProvideError() {
        film.setDescription(notCorrectDescription);
        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), descriptionSizeValidationViolation);
    }

    @Test
    void createFilmRealiseDateEarlierDateOfFirstFilmProvideError() {
        film.setReleaseDate(notCorrectReleaseDate);
        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), releaseDateBorderValidationViolation);
    }

    @Test
    void createFilmDurationNegativeValueProvideError() {
        film.setDuration(notCorrectDuration);
        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), negativeDurationValidationViolation);
    }

    @Test
    void updateFilmWithoutIdThrowsException() {
        filmController.addFilm(film);
        updatedFilm.setId(null);
        assertThrows(NotFoundException.class, () -> filmController.updateFilm(updatedFilm),
                "Обновление фильма без ID должно приводить к ошибке");
    }

    @Test
    void correctFilmUpdate() {
        filmController.addFilm(film);
        filmController.updateFilm(updatedFilm);
        List<FilmDto> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size(), returnFilmsListNotCorrectSize);
        FilmDto filmFromController = filmList.get(0);
        assertEquals(updatedFilm.getName(), filmFromController.getName(), returnNotCorrectFilm);
    }

    @Test
    void updateEmptyNameFilmProvideError() {
        filmController.addFilm(film);
        updatedFilm.setName(notCorrectName);
        Set<ConstraintViolation<FilmUpdateRequest>> violations = validator.validate(updatedFilm);
        assertFalse(violations.isEmpty(), emptyNameValidationViolation);
    }

    @Test
    void updateFilmDescriptionLengthMore200ProvideError() {
        filmController.addFilm(film);
        updatedFilm.setDescription(notCorrectDescription);
        Set<ConstraintViolation<FilmUpdateRequest>> violations = validator.validate(updatedFilm);
        assertFalse(violations.isEmpty(), descriptionSizeValidationViolation);
    }

    @Test
    void updateFilmRealiseDateEarlierDateOfFirstFilmProvideError() {
        filmController.addFilm(film);
        updatedFilm.setReleaseDate(notCorrectReleaseDate);
        Set<ConstraintViolation<FilmUpdateRequest>> violations = validator.validate(updatedFilm);
        assertFalse(violations.isEmpty(), releaseDateBorderValidationViolation);
    }

    @Test
    void updateFilmDurationNegativeValueProvideError() {
        filmController.addFilm(film);
        updatedFilm.setDuration(notCorrectDuration);
        Set<ConstraintViolation<FilmUpdateRequest>> violations = validator.validate(updatedFilm);
        assertFalse(violations.isEmpty(), negativeDurationValidationViolation);
    }

    @Test
    void correctReturnAllFilms() {
        filmController.addFilm(film);
        FilmCreateRequest film2 = testObjects.film2;
        filmController.addFilm(film2);
        List<FilmDto> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size(), returnFilmsListNotCorrectSize);
        assertEquals(film.getName(), filmController.getAllFilms().get(0).getName(), returnNotCorrectFilm);
        assertEquals(film2.getName(), filmController.getAllFilms().get(1).getName(), returnNotCorrectFilm);
    }
}