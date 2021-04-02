package ru.nsu.fit.cswd.intelli_games.services.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.fit.cswd.intelli_games.domain.Game;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.model.OperationResult;

import java.time.LocalDateTime;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public enum TestGames {
    SUCCESS(new GameDto(null, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
            1, 1, 1, 1, Collections.emptyList()),
            new Game(null, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
            1, 1, 1, 1, null, Collections.emptyList()),
            OperationResult.SUCCESS),
    DB_ERROR(new GameDto(null, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
            1, 1, 1, 1, Collections.emptyList()),
            new Game(null, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
                    1, 1, 1, 1, null, Collections.emptyList()),
            OperationResult.DB_ERROR),
    NOT_FOUND(new GameDto(1L, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
            1, 1, 1, 1, Collections.emptyList()),
            new Game(null, "test", LocalDateTime.parse("2020-11-20T21:51:20.193376400"),
                    1, 1, 1, 1, null, Collections.emptyList()),
            OperationResult.NOT_FOUND);

    private final GameDto gameDto;
    private final Game game;
    private final OperationResult result;
}
