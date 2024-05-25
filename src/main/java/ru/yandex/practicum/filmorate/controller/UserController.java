package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody @Validated(Create.class) @NonNull User user) {
        userValidationControl(user);
        user.setId(getNextNextId());
        ifNameEmptyFillWithLogin(user);
        users.put(user.getId(), user);
        log.info("Created user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated(Update.class) @NonNull User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Attempt to update user with unknown id={}:\n{}", user.getId(), user);
            throw new ValidationException("Пользователя с таким ID не зарегестрировано");
        }
        userValidationControl(user);
        ifNameEmptyFillWithLogin(user);
        users.put(user.getId(), user);
        log.info("Updated user with id={}:\n{}", user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private Integer getNextNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void ifNameEmptyFillWithLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void userValidationControl(User user) {
        if (user.getEmail().isBlank()) {
            log.warn("Attempt to use User with empty email id={}:\n{}", user.getId(), user);
            throw new ValidationException("E-mail не может быть пустым");
        } else if (!user.getEmail().matches("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)")) {
            log.warn("Attempt to use User with incorrect email id={}:\n{}", user.getId(), user);
            throw new ValidationException("Указан некорректный e-mail");
        } else if (user.getLogin().isBlank()) {
            log.warn("Attempt to use User with empty login id={}:\n{}", user.getId(), user);
            throw new ValidationException("Логин не может быть пустым");
        } else if (!user.getLogin().matches("^\\S+$")) {
            log.warn("Attempt to use User login with spaces id={}:\n{}", user.getId(), user);
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Attempt to use User with birthday in future id={}:\n{}", user.getId(), user);
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}