package ru.yandex.practicum.filmorate.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.FriendshipStatus;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Map<Integer, FriendshipStatus> friendsId;
}
