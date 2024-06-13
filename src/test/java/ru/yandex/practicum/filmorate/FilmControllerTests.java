package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.service.DefaultFilmService;
import ru.yandex.practicum.filmorate.service.DefaultUserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTests {

    private FilmController filmController;
    private Film film;
    private Film updatedFilm;
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
        filmController = new FilmController(new DefaultFilmService(new InMemoryFilmStorage(),
                new DefaultUserService(new InMemoryUserStorage())));
        film = new Film(null, "name1", "Description1",
                LocalDate.of(2000, 10, 15), Duration.ofMinutes(120), new HashSet<>());
        updatedFilm = new Film(1, "updatedName", "UpdatedDescription",
                LocalDate.of(2010, 1, 1), Duration.ofMinutes(180), new HashSet<>());
    }

    @Test
    void filmIsNullThrowException() {
        assertThrows(NullPointerException.class, () -> filmController.addFilm(null),
                "Пустой запрос должен приводить к ошибке");
    }

    @Test
    void correctFilmCreated() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Корректный фильм не проходит валидацию");
        filmController.addFilm(film);
        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size(), returnFilmsListNotCorrectSize);
        Film filmFromController = filmController.getAllFilms().get(0);
        film.setId(1);
        assertEquals(film, filmFromController, returnNotCorrectFilm);
    }

    @Test
    void createdEmptyNameFilmValidationsProvideError() {
        film.setName(notCorrectName);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), emptyNameValidationViolation);
    }

    @Test
    void createFilmDescriptionLengthMore200ProvideError() {
        film.setDescription(notCorrectDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), descriptionSizeValidationViolation);
    }

    @Test
    void createFilmRealiseDateEarlierDateOfFirstFilmProvideError() {
        film.setReleaseDate(notCorrectReleaseDate);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), releaseDateBorderValidationViolation);
    }

    @Test
    void createFilmDurationNegativeValueProvideError() {
        film.setDuration(notCorrectDuration);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), negativeDurationValidationViolation);
    }

    @Test
    void updateFilmWithoutIdThrowsException() {
        filmController.addFilm(film);
        updatedFilm.setId(null);
        assertThrows(FilmNotFoundException.class, () -> filmController.updateFilm(updatedFilm),
                "Обновление фильма без ID должно приводить к ошибке");
    }

    @Test
    void correctFilmUpdate() {
        filmController.addFilm(film);
        filmController.updateFilm(updatedFilm);
        List<Film> filmList = filmController.getAllFilms();
        assertEquals(1, filmList.size(), returnFilmsListNotCorrectSize);
        Film filmFromController = filmController.getAllFilms().get(0);
        assertEquals(updatedFilm, filmFromController, returnNotCorrectFilm);
    }

    @Test
    void updateEmptyNameFilmProvideError() {
        filmController.addFilm(film);
        updatedFilm.setName(notCorrectName);
        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm, Update.class);
        assertFalse(violations.isEmpty(), emptyNameValidationViolation);
    }

    @Test
    void updateFilmDescriptionLengthMore200ProvideError() {
        filmController.addFilm(film);
        updatedFilm.setDescription(notCorrectDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm, Update.class);
        assertFalse(violations.isEmpty(), descriptionSizeValidationViolation);
    }

    @Test
    void updateFilmRealiseDateEarlierDateOfFirstFilmProvideError() {
        filmController.addFilm(film);
        updatedFilm.setReleaseDate(notCorrectReleaseDate);
        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm, Update.class);
        assertFalse(violations.isEmpty(), releaseDateBorderValidationViolation);
    }

    @Test
    void updateFilmDurationNegativeValueProvideError() {
        filmController.addFilm(film);
        updatedFilm.setDuration(notCorrectDuration);
        Set<ConstraintViolation<Film>> violations = validator.validate(updatedFilm, Update.class);
        assertFalse(violations.isEmpty(), negativeDurationValidationViolation);
    }

    @Test
    void correctReturnAllFilms() {
        filmController.addFilm(film);
        Film film2 = new Film(null, "name2", "description2",
                LocalDate.of(2010, 1, 1), Duration.ofMinutes(180), new HashSet<>());
        filmController.addFilm(film2);
        List<Film> filmList = filmController.getAllFilms();
        assertEquals(2, filmList.size(), returnFilmsListNotCorrectSize);
        film.setId(1);
        assertEquals(film, filmController.getAllFilms().get(0), returnNotCorrectFilm);
        film2.setId(2);
        assertEquals(film2, filmController.getAllFilms().get(1), returnNotCorrectFilm);
    }
}