package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dal.mapper.FriendsExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Primary
@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private final FriendsExtractor extractor;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
        extractor = new FriendsExtractor();
    }

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT user2_id AS user_id," +
                                                         "user1_id AS friend_id, " +
                                                         "'APPROVED' AS status " +
                                                         "FROM accepted_friends " +
                                                         "UNION " +
                                                         "SELECT user1_id  AS user_id, " +
                                                         "user2_id AS friend_id, " +
                                                         "'APPROVED' AS status " +
                                                         "FROM accepted_friends " +
                                                         "UNION " +
                                                         "SELECT user_id  AS user_id, " +
                                                         "friend_id AS friend_id, " +
                                                         "'REQUESTED' AS status " +
                                                         "FROM REQUESTED_FRIENDS;";
    private static final String FIND_BY_ID_FRIENDS_QUERY = "SELECT user2_id AS user_id," +
                                                           "user1_id AS friend_id, " +
                                                           "'APPROVED' AS status " +
                                                           "FROM accepted_friends " +
                                                           "WHERE user2_id = ? " +
                                                           "UNION " +
                                                           "SELECT user1_id  AS user_id, " +
                                                           "user2_id AS friend_id, " +
                                                           "'APPROVED' AS status " +
                                                           "FROM accepted_friends " +
                                                           "WHERE user1_id = ? " +
                                                           "UNION " +
                                                           "SELECT user_id  AS user_id, " +
                                                           "friend_id AS friend_id, " +
                                                           "'REQUESTED' AS status " +
                                                           "FROM REQUESTED_FRIENDS " +
                                                           "WHERE user_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
                                               "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                                               "WHERE user_id = ?";

    @Override
    public User createUser(User user) {
        Integer userId = insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(userId);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        Map<Integer, Map<Integer, FriendshipStatus>> allFriendsMap = jdbc.query(FIND_ALL_FRIENDS_QUERY, extractor);
        List<User> users = jdbc.query(FIND_ALL_QUERY, mapper);
        if (allFriendsMap != null && !allFriendsMap.isEmpty()) {
            for (User user : users) {
                Integer userId = user.getId();
                if ((allFriendsMap.containsKey(userId))) {
                    user.setFriendsId(allFriendsMap.get(userId));
                } else {
                    user.setFriendsId(new HashMap<>());
                }
            }
        }
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        Map<Integer, Map<Integer, FriendshipStatus>> friendsMap =
                jdbc.query(FIND_BY_ID_FRIENDS_QUERY, extractor, id, id, id);
        try {
            User user = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            if (user != null && friendsMap != null && !friendsMap.isEmpty()) {
                user.setFriendsId(friendsMap.get(id));
            }
            return user;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }
}
