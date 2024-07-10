package ru.yandex.practicum.filmorate.storage.dal.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class FriendsExtractor implements ResultSetExtractor<Map<Integer, Map<Integer, FriendshipStatus>>> {
    @Override
    public Map<Integer, Map<Integer, FriendshipStatus>> extractData(ResultSet rs)
            throws SQLException, DataAccessException {
        Map<Integer, Map<Integer, FriendshipStatus>> data = new HashMap<>();
        while (rs.next()) {
            Integer userId = rs.getInt("user_id");
            data.putIfAbsent(userId, new HashMap<>());
            Integer friendId = rs.getInt("friend_id");
            FriendshipStatus status = FriendshipStatus.valueOf(rs.getString("status"));
            data.get(userId).put(friendId, status);
        }
        return data;
    }
}
