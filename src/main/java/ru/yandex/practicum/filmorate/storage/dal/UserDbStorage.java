package ru.yandex.practicum.filmorate.storage.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dal.mapper.FriendsExtractor;
import ru.yandex.practicum.filmorate.storage.dal.mapper.UserRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Primary
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;
    private final FriendsExtractor extractor;

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

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
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
        User user  = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
        if (user != null && friendsMap != null && !friendsMap.isEmpty()) {
            user.setFriendsId(friendsMap.get(id));
        }
        return user;
    }
}
