package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.Create;

import java.time.LocalDate;

@Data
public class UserCreateRequest {
    @Email(groups = {Create.class}, message = "Введен некорректный e-mail")
    @NotBlank(groups = {Create.class}, message = "E-mail не может быть пустым")
    private String email;
    @NotBlank(groups = {Create.class}, message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", groups = {Create.class}, message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(groups = {Create.class}, message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
