package ru.nsu.fit.cswd.intelli_games.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String login;
    @NotNull
    @Size(min = 1)
    private String name;
    @NotNull
    @Size(min = 1)
    private String password;
    @NotNull
    @Size(min = 1)
    private String passwordConfirmation;
    @NotNull
    @Size(min = 1)
    private String question;
    @NotNull
    @Size(min = 1)
    private String answer;
    private UserRole role;

    public UserDto() {
        role = UserRole.Player;
    }
}
