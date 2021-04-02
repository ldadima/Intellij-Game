package ru.nsu.fit.cswd.intelli_games.services.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;
import ru.nsu.fit.cswd.intelli_games.model.RegistrationResult;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;

@Getter
@RequiredArgsConstructor
public enum TestUsers {
    DB_ERROR(new UserDto(1L,"test", "test","test","test",
            "test","test",  UserRole.Player),
            new User(1L, "test","test","test",
                    "test","test",  UserRole.Player, null),
            RegistrationResult.DB_ERROR),
    PASSWORD_ERROR(new UserDto(1L,"test", "test","test","testError",
                    "test","test",  UserRole.Player),
             new User(1L, "test","test","test",
                     "test","test",  UserRole.Player, null),
            RegistrationResult.PASSWORDS_DONT_MATCH
            ),
    SUCCESS(new UserDto(1L,"test", "test","test","test",
                    "test","test",  UserRole.Player),
            new User(1L, "test","test","test",
            "test","test",  UserRole.Player, null),
            RegistrationResult.SUCCESS
            );

    private final UserDto userDto;
    private final User user;
    private final RegistrationResult result;

}
