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
import ru.yandex.practicum.filmorate.dto.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.mapper.UserRowMapper;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:test-data-before.sql"}),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:test-data-after.sql"})
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class})
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;
    private TestObjects testObjects;
    private User user;
    private User updatedUser;

    private final String returnNotCorrectUser = "Пользователь в базе данных не равен соответствует отправленному";
    private final String returnUserListNotCorrectSize = "Некорректный размер списка пользователей";
    private final String returnUserFriendsListNotCorrectSize = "Некорректный размер списка друзей пользователей";

    @BeforeEach
    public void beforeEach() {
        testObjects = new TestObjects();
        user = UserMapper.userCreatedRequestToUser(testObjects.user);
        updatedUser = UserMapper.updateUserFields(user, testObjects.updatedUser);
    }

    @Test
    public void getAllUsersTest() {
        List<User> userList = userDbStorage.getAllUsers();
        assertEquals(4, userList.size(), returnUserListNotCorrectSize);
    }

    @Test
    public void createUserTest() {
        userDbStorage.createUser(user);
        List<User> userList = userDbStorage.getAllUsers();
        assertEquals(5, userList.size(), returnUserListNotCorrectSize);
        User userFromDb = userList.get(4);
        user.setId(5);
        user.setFriendsId(new HashMap<>());
        assertEquals(user, userFromDb, returnNotCorrectUser);
    }

    @Test
    public void updateUserTest() {
        userDbStorage.createUser(user);
        updatedUser.setId(5);
        userDbStorage.updateUser(updatedUser);
        List<User> userList = userDbStorage.getAllUsers();
        assertEquals(5, userList.size(), returnUserListNotCorrectSize);
        User userFromDb = userList.get(4);
        assertEquals(updatedUser, userFromDb, returnNotCorrectUser);
    }

    @Test
    public void getUserByIdTest() {
        userDbStorage.createUser(user);
        user.setId(5);
        user.setFriendsId(new HashMap<>());
        User userFromDb = userDbStorage.getUserById(user.getId());
        assertEquals(user, userFromDb, returnNotCorrectUser);
    }

    @Test
    public void addFriendTest() {
        userDbStorage.createUser(user);
        user.setId(5);
        user.setFriendsId(new HashMap<>());
        userDbStorage.addFriend(user, 2, FriendshipStatus.REQUESTED);
        userDbStorage.addFriend(user, 3, FriendshipStatus.REQUESTED);
        User userFromDb = userDbStorage.getUserById(user.getId());
        user.getFriendsId().put(2, FriendshipStatus.REQUESTED);
        user.getFriendsId().put(3, FriendshipStatus.REQUESTED);
        assertEquals(user, userFromDb, returnNotCorrectUser);
    }

    @Test
    public void deleteFriendTest() {
        User userFromDb = userDbStorage.getUserById(1);
        int frendsBefore = userFromDb.getFriendsId().size();
        userDbStorage.deleteFriend(userFromDb, 3, FriendshipStatus.REQUESTED);
        userFromDb = userDbStorage.getUserById(1);
        Integer frendsAfter = userFromDb.getFriendsId().size();
        assertEquals(frendsBefore - 1, frendsAfter, returnUserFriendsListNotCorrectSize);
    }
}
