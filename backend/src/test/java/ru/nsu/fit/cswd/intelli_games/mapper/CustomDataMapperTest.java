package ru.nsu.fit.cswd.intelli_games.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.fit.cswd.intelli_games.domain.Game;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;
import ru.nsu.fit.cswd.intelli_games.services.model.TestGames;
import ru.nsu.fit.cswd.intelli_games.services.model.TestUsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomDataMapperTest {
    private final CustomDataMapper customDataMapper = Mappers.getMapper(CustomDataMapper.class);

    @Test
    void toBdUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        UserDto dto = TestUsers.SUCCESS.getUserDto();
        User expected = TestUsers.SUCCESS.getUser();
        User res = customDataMapper.toDbUser(dto, passwordEncoder);
        expected.setPassword(res.getPassword());
        assertEquals(expected, res);
    }

    @Test
    void toGameDto() {
        GameDto gameDto = new GameDto();
        customDataMapper.toGameDtoCustom(TestGames.SUCCESS.getGame(), gameDto);
        assertEquals(TestGames.SUCCESS.getGameDto(), gameDto);
    }

    @Test
    void toGameEntity() {
        Game game = new Game();
        customDataMapper.toGameEntity(TestGames.SUCCESS.getGameDto(), game);
        assertEquals(TestGames.SUCCESS.getGame(), game);
    }
}