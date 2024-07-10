package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.dto.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mapper.FilmRowMapper;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:test-data-before.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class, FilmRowMapper.class})
public class FilmDbStorageTests {
    private final FilmDbStorage filmDbStorage;
    private TestObjects testObjects;
    private Film film;
    private Film updatedFilm;

    private final String returnNotCorrectFilm = "Фильм в базе данных не равен соответствует отправленному";
    private final String returnFilmsListNotCorrectSize = "Некорректный размер списка фильмов";
    private final String returnUserLikesIdListNotCorrectSize = "Некорректный размер списка id пользователей поставивших лайк";

    @BeforeEach
    public void beforeEach() {
        testObjects = new TestObjects();
        film = FilmMapper.filmCreatedRequestToFilm(testObjects.film);
        updatedFilm = FilmMapper.updateFilmFields(film, testObjects.updatedFilm);
    }

    @Test
    public void getAllFilmsTest() {
        List<Film> filmList = filmDbStorage.getAllFilms();
        assertEquals(4, filmList.size(), returnFilmsListNotCorrectSize);
    }

    @Test
    public void createFilmTest() {
        filmDbStorage.addFilm(film);
        List<Film> filmList = filmDbStorage.getAllFilms();
        assertEquals(5, filmList.size(), returnFilmsListNotCorrectSize);
        Film filmFromDb = filmList.get(4);
        film.setId(5);
        film.setUsersIdPostedLikes(new HashSet<>());
        assertEquals(film, filmFromDb, returnNotCorrectFilm);
    }

    @Test
    public void updateFilmTest() {
        filmDbStorage.addFilm(film);
        updatedFilm.setId(5);
        filmDbStorage.updateFilm(updatedFilm);
        List<Film> filmList = filmDbStorage.getAllFilms();
        assertEquals(5, filmList.size(), returnFilmsListNotCorrectSize);
        Film filmFromDb = filmList.get(4);
        assertEquals(updatedFilm, filmFromDb, returnNotCorrectFilm);
    }

    @Test
    public void getFilmByIdTest() {
        filmDbStorage.addFilm(film);
        film.setId(5);
        film.setUsersIdPostedLikes(new HashSet<>());
        Film filmFromDb = filmDbStorage.getFilmById(film.getId());
        assertEquals(film, filmFromDb, returnNotCorrectFilm);
    }

    @Test
    public void userPostLikeToFilmTest() {
        filmDbStorage.addFilm(film);
        film.setId(5);
        film.setUsersIdPostedLikes(new HashSet<>());
        filmDbStorage.userPostLikeToFilm(film, 1);
        filmDbStorage.userPostLikeToFilm(film, 2);
        film.setUsersIdPostedLikes(new HashSet<>());
        film.getUsersIdPostedLikes().add(1);
        film.getUsersIdPostedLikes().add(2);
        Film filmFromDb = filmDbStorage.getFilmById(film.getId());
        assertEquals(film, filmFromDb, returnUserLikesIdListNotCorrectSize);
    }

    @Test
    public void userDeleteLikeToFilmTest() {
        Film filmFromDb = filmDbStorage.getFilmById(1);
        int likesBefore = filmFromDb.getUsersIdPostedLikes().size();
        filmDbStorage.userDeleteLikeToFilm(filmFromDb, 1);
        filmFromDb = filmDbStorage.getFilmById(1);
        int likesAfter = filmFromDb.getUsersIdPostedLikes().size();
        assertEquals(likesBefore - 1, likesAfter, returnUserLikesIdListNotCorrectSize);
    }
}
