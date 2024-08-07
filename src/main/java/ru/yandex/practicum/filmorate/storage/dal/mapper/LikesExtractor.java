package ru.yandex.practicum.filmorate.storage.dal.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class LikesExtractor implements ResultSetExtractor<Map<Integer, Set<Integer>>> {
    @Override
    public Map<Integer, Set<Integer>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Set<Integer>> data = new HashMap<>();
        while (rs.next()) {
            Integer filmId = rs.getInt("film_id");
            data.putIfAbsent(filmId, new HashSet<>());
            Integer userId = rs.getInt("user_id");
            data.get(filmId).add(userId);
        }
        return data;
    }
}
