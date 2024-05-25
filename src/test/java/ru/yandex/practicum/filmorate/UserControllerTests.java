package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {

    private UserController userController;
    private User user;
    private User updatedUser;

    private final String emptyEmail = " ";
    private final String notCorrectEmail = "notCorrectEmail";
    private final String emptyLogin = " ";
    private final String notCorrectLogin = "lo gin";
    private final String emptyName = " ";
    private final LocalDate notCorrectBirthday = LocalDate.of(2030, 10, 15);

    private final String returnNotCorrectUser = "Пользователь в базе данных не равен соответствует отправленному";
    private final String returnUserListNotCorrectSize = "Некорректный размер списка пользователей";
    private final String emptyEmailValidationViolation = "Пользователь с пустым email должен приводить к ошибке";
    private final String notCorrectEmailValidationViolation = "Пользователь с некорректным email должен приводить к ошибке";
    private final String emptyLoginValidationViolation = "Пользователь с пустым email должен приводить к ошибке";
    private final String notCorrectLoginValidationViolation = "Пользователь с некорректным email должен приводить к ошибке";
    private final String nameUseLoginIfEmptyViolation = "Имя для отображения не равно логину";
    private final String birthdayValidationViolation = "Пользователь с датой рожедния в будущем должен приводить к ошибке";


    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(null, "name@mail.ru", "login", "name",
                LocalDate.of(2000, 10, 15));
        updatedUser = new User(1, "organization@mail.ru", "newLogin", "Surname",
                LocalDate.of(1989, 7, 27));
    }

    @Test
    void userIsNullThrowException() {
        assertThrows(NullPointerException.class, () -> userController.createUser(null),
                "Пустой запрос должен приводить к ошибке");
    }

    @Test
    void correctUserCreated() {
        userController.createUser(user);
        List<User> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        User userFromController = userController.getAllUsers().get(0);
        user.setId(1);
        assertEquals(user, userFromController, returnNotCorrectUser);
    }

    @Test
    void createEmptyEmailUserThrowsException() {
        user.setEmail(emptyEmail);
        assertThrows(ValidationException.class, () -> userController.createUser(user),
                emptyEmailValidationViolation);
    }

    @Test
    void createNotCorrectEmailUserThrowsException() {
        user.setEmail(notCorrectEmail);
        assertThrows(ValidationException.class, () -> userController.createUser(user),
                notCorrectEmailValidationViolation);
    }

    @Test
    void createEmptyLoginUserThrowsException() {
        user.setLogin(emptyLogin);
        assertThrows(ValidationException.class, () -> userController.createUser(user),
                emptyLoginValidationViolation);
    }

    @Test
    void createNotCorrectLoginUserThrowsException() {
        user.setLogin(notCorrectLogin);
        assertThrows(ValidationException.class, () -> userController.createUser(user),
                notCorrectLoginValidationViolation);
    }

    @Test
    void createUserWithoutNameUseLoginInstead() {
        user.setName(emptyName);
        userController.createUser(user);
        List<User> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        User userFromController = userController.getAllUsers().get(0);
        assertEquals(user.getLogin(), userFromController.getName(), nameUseLoginIfEmptyViolation);
    }

    @Test
    void createNotCorrectBirthdayUserThrowsException() {
        user.setBirthday(notCorrectBirthday);
        assertThrows(ValidationException.class, () -> userController.createUser(user),
                birthdayValidationViolation);
    }

    @Test
    void updateUserWithoutIdThrowsException() {
        userController.createUser(user);
        updatedUser.setId(null);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                "Обновление пользователя без ID должно приводить к ошибке");
    }

    @Test
    void correctUserUpdate() {
        userController.createUser(user);
        userController.updateUser(updatedUser);
        List<User> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        User userFromController = userController.getAllUsers().get(0);
        assertEquals(updatedUser, userFromController, returnNotCorrectUser);
    }

    @Test
    void updateEmptyEmailUserThrowsException() {
        userController.createUser(user);
        updatedUser.setEmail(emptyEmail);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                emptyEmailValidationViolation);
    }

    @Test
    void updateNotCorrectEmailUserThrowsException() {
        userController.createUser(user);
        updatedUser.setEmail(notCorrectEmail);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                notCorrectEmailValidationViolation);
    }

    @Test
    void updateEmptyLoginUserThrowsException() {
        userController.createUser(user);
        updatedUser.setLogin(emptyLogin);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                emptyLoginValidationViolation);
    }

    @Test
    void updateNotCorrectLoginUserThrowsException() {
        userController.createUser(user);
        updatedUser.setLogin(notCorrectLogin);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                notCorrectLoginValidationViolation);
    }

    @Test
    void updateUserWithoutNameUseLoginInstead() {
        userController.createUser(user);
        updatedUser.setName(emptyName);
        userController.updateUser(updatedUser);
        List<User> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        User userFromController = userController.getAllUsers().get(0);
        assertEquals(updatedUser.getLogin(), userFromController.getName(), nameUseLoginIfEmptyViolation);
    }

    @Test
    void updateNotCorrectBirthdayUserThrowsException() {
        userController.createUser(user);
        updatedUser.setBirthday(notCorrectBirthday);
        assertThrows(ValidationException.class, () -> userController.updateUser(updatedUser),
                birthdayValidationViolation);
    }

    @Test
    void correctReturnAllUsers() {
        userController.createUser(user);
        User user2 = new User(null, "organization@mail.ru", "newLogin", "Surname",
                LocalDate.of(1989, 7, 27));
        userController.createUser(user2);
        List<User> userList = userController.getAllUsers();
        assertEquals(2, userList.size(), returnUserListNotCorrectSize);
        user.setId(1);
        assertEquals(user, userController.getAllUsers().get(0), returnNotCorrectUser);
        user2.setId(2);
        assertEquals(user2, userController.getAllUsers().get(1), returnNotCorrectUser);
    }
}