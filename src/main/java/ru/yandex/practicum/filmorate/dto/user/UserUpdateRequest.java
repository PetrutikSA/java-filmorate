package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.Update;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    @Min(value = 1, groups = {Update.class}, message = "ID долно быть положжительным числом")
    @NotNull
    private Integer id;
    @Email(groups = {Update.class}, message = "Введен некорректный e-mail")
    @NotBlank(groups = {Update.class}, message = "E-mail не может быть пустым")
    private String email;
    @NotBlank(groups = {Update.class}, message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", groups = {Update.class}, message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(groups = {Update.class}, message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
