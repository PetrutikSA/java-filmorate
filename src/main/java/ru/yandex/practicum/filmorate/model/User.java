package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.validator.Create;
import ru.yandex.practicum.filmorate.model.validator.Update;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Min(value = 1, groups = {Update.class}, message = "ID долно быть положжительным числом")
    private Integer id;
    @Email(groups = {Create.class, Update.class}, message = "Введен некорректный e-mail")
    @NotBlank(groups = {Create.class, Update.class}, message = "E-mail не может быть пустым")
    private String email;
    @NotBlank(groups = {Create.class, Update.class}, message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", groups = {Create.class, Update.class}, message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(groups = {Create.class, Update.class}, message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Map<Integer, FriendshipStatus> friendsId;
}

