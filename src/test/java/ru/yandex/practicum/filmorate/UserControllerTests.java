package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.service.DefaultUserService;
import ru.yandex.practicum.filmorate.storage.inmemorry.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {
    private UserController userController;
    private UserCreateRequest user;
    private UserUpdateRequest updatedUser;

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
    private final String birthdayValidationViolation = "Пользователь с датой рождения в будущем должен приводить к ошибке";
    private Validator validator;


    @BeforeEach
    void beforeEach() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        userController = new UserController(new DefaultUserService(new InMemoryUserStorage()));
        user = new UserCreateRequest("name@mail.ru", "login", "name", LocalDate.of(2000, 10, 15));
        updatedUser = new UserUpdateRequest(1, "organization@mail.ru", "newLogin", "Surname",
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
        List<UserDto> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        UserDto userFromController = userController.getAllUsers().get(0);
        assertEquals(user.getEmail(), userFromController.getEmail(), returnNotCorrectUser);
    }

    @Test
    void createEmptyEmailUserProvideError() {
        user.setEmail(emptyEmail);
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), emptyEmailValidationViolation);
    }

    @Test
    void createNotCorrectEmailUserProvideError() {
        user.setEmail(notCorrectEmail);
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), notCorrectEmailValidationViolation);
    }

    @Test
    void createEmptyLoginUserProvideError() {
        user.setLogin(emptyLogin);
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), emptyLoginValidationViolation);
    }

    @Test
    void createNotCorrectLoginUserProvideError() {
        user.setLogin(notCorrectLogin);
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), notCorrectLoginValidationViolation);
    }

    @Test
    void createUserWithoutNameUseLoginInstead() {
        user.setName(emptyName);
        userController.createUser(user);
        List<UserDto> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        UserDto userFromController = userList.get(0);
        assertEquals(user.getLogin(), userFromController.getName(), nameUseLoginIfEmptyViolation);
    }

    @Test
    void createNotCorrectBirthdayUserProvideError() {
        user.setBirthday(notCorrectBirthday);
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(user, Create.class);
        assertFalse(violations.isEmpty(), birthdayValidationViolation);
    }

    @Test
    void updateUserWithoutIdThrowsException() {
        userController.createUser(user);
        updatedUser.setId(null);
        assertThrows(UserNotFoundException.class, () -> userController.updateUser(updatedUser),
                "Обновление пользователя без ID должно приводить к ошибке");
    }

    @Test
    void correctUserUpdate() {
        userController.createUser(user);
        userController.updateUser(updatedUser);
        List<UserDto> userList = userController.getAllUsers();
        assertEquals(1, userList.size(), returnUserListNotCorrectSize);
        UserDto userFromController = userList.get(0);
        assertEquals(updatedUser.getEmail(), userFromController.getEmail(), returnNotCorrectUser);
    }

    @Test
    void updateEmptyEmailUserProvideError() {
        updatedUser.setEmail(emptyEmail);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(updatedUser, Update.class);
        assertFalse(violations.isEmpty(), emptyEmailValidationViolation);
    }

    @Test
    void updateNotCorrectEmailUserProvideError() {
        updatedUser.setEmail(notCorrectEmail);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(updatedUser, Update.class);
        assertFalse(violations.isEmpty(), notCorrectEmailValidationViolation);
    }

    @Test
    void updateEmptyLoginUserProvideError() {
        updatedUser.setLogin(emptyLogin);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(updatedUser, Update.class);
        assertFalse(violations.isEmpty(), emptyLoginValidationViolation);
    }

    @Test
    void updateNotCorrectLoginUserProvideError() {
        updatedUser.setLogin(notCorrectLogin);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(updatedUser, Update.class);
        assertFalse(violations.isEmpty(), notCorrectLoginValidationViolation);
    }

    @Test
    void updateNotCorrectBirthdayUserProvideError() {
        updatedUser.setBirthday(notCorrectBirthday);
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(updatedUser, Update.class);
        assertFalse(violations.isEmpty(), birthdayValidationViolation);
    }

    @Test
    void correctReturnAllUsers() {
        userController.createUser(user);
        UserCreateRequest user2 = new UserCreateRequest("organization@mail.ru", "newLogin", "Surname",
                LocalDate.of(1989, 7, 27));
        userController.createUser(user2);
        List<UserDto> userList = userController.getAllUsers();
        assertEquals(2, userList.size(), returnUserListNotCorrectSize);
        assertEquals(user.getEmail(), userController.getAllUsers().get(0).getEmail(), returnNotCorrectUser);
        assertEquals(user2.getEmail(), userController.getAllUsers().get(1).getEmail(), returnNotCorrectUser);
    }
}