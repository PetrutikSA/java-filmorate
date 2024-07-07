package ru.yandex.practicum.filmorate.storage.dal.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.enums.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GenreExtractor implements ResultSetExtractor<Map<Integer, Set<Genre>>> {
    @Override
    public Map<Integer, Set<Genre>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Set<Genre>> data = new HashMap<>();
        while(rs.next()) {
            Integer filmId = rs.getInt("film_id");
            data.putIfAbsent(filmId, new LinkedHashSet<>());
            Integer genreId = rs.getInt("genre_id");
            data.get(filmId).add(Genre.forValues(genreId));
        }
        return data;
    }
}
