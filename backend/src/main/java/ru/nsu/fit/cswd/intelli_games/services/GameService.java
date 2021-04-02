package ru.nsu.fit.cswd.intelli_games.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.nsu.fit.cswd.intelli_games.domain.Game;
import ru.nsu.fit.cswd.intelli_games.domain.Question;
import ru.nsu.fit.cswd.intelli_games.domain.Team;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.dto.QuestionDto;
import ru.nsu.fit.cswd.intelli_games.dto.admin.AdminGameDto;
import ru.nsu.fit.cswd.intelli_games.dto.player.PlayerGameDto;
import ru.nsu.fit.cswd.intelli_games.mapper.CustomDataMapper;
import ru.nsu.fit.cswd.intelli_games.model.OperationResult;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;
import ru.nsu.fit.cswd.intelli_games.model.exceptions.NotFoundException;
import ru.nsu.fit.cswd.intelli_games.repositories.GameRepository;
import ru.nsu.fit.cswd.intelli_games.repositories.QuestionRepository;
import ru.nsu.fit.cswd.intelli_games.repositories.TeamRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final CustomDataMapper customDataMapper;
    private final UserDetailsServiceImpl userDetailsService;


    public Long create(GameDto gameDto) {
        gameRepository.findByDateStart(gameDto.getDateStart()).ifPresent(g -> {
            throw new IllegalArgumentException("Game with start date " + g.getDateStart() + " already exists");
        });

        Game game = new Game();
        customDataMapper.toGameEntity(gameDto, game);
        game = gameRepository.save(game);

        return game.getId();
    }

    public OperationResult update(GameDto gameDto) {
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found", gameDto.getId())));

        customDataMapper.toGameEntity(gameDto, game);
        gameRepository.save(game);
        return OperationResult.SUCCESS;
    }

    public GameDto findGame(long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found", id)));

        return customDataMapper.toGameDtoCustom(game, new GameDto());
    }

    public Page<GameDto> findAll(Integer page, Integer size, User user) {
        if (page == null || size == null) {
            throw new IllegalArgumentException("Not specified size or page");
        }
        List<GameDto> gameDtos = new ArrayList<>();
        Page<GameDto> gamePage;
        Page<Game> games;
        if (UserRole.Admin.equals(user.getRole())) {
            games = gameRepository.findAll(PageRequest.of(page, size, Sort.by("dateStart").ascending()));
            for (Game game : games.toList()) {
                AdminGameDto adminGameDto = new AdminGameDto();
                customDataMapper.toGameDtoCustom(game, adminGameDto);
                adminGameDto.setApplications(game.getTeams().size());
                gameDtos.add(adminGameDto);
            }
        } else {
            games = gameRepository.findGamesByDateStartAfter(PageRequest.of(page, size, Sort.by("dateStart").ascending()), LocalDateTime.now());
            for (Game game : games.toList()) {
                PlayerGameDto playerGameDto = new PlayerGameDto();
                customDataMapper.toGameDtoCustom(game, playerGameDto);
                boolean participate = Optional.of(user)
                        .map(User::getTeam)
                        .map(Team::getGame)
                        .map(game1 -> game1.getId().equals(game.getId()))
                        .orElse(false);
                playerGameDto.setParticipation(participate);
                gameDtos.add(playerGameDto);
            }
        }
        gamePage = new PageImpl<>(gameDtos, games.getPageable(), games.getTotalElements());
        return gamePage;
    }

    public OperationResult participate(GameDto gameDto) {
        User user = userDetailsService.reloadPrincipal();
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found", gameDto.getId())));

        Team team = user.getTeam();
        team.setGame(game);

        teamRepository.save(team);

        return OperationResult.SUCCESS;
    }

    public OperationResult cancelParticipate() {
        User user = userDetailsService.reloadPrincipal();
        Team team = user.getTeam();
        team.setGame(null);

        teamRepository.save(team);

        return OperationResult.SUCCESS;
    }


    public void deleteGame(long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found", gameId)));

        gameRepository.delete(game);
    }

    public void updateQuestions(long gameId, List<QuestionDto> questionDtos) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(String.format("Game with id %d not found", gameId)));

        game.getQuestions().clear();
        gameRepository.save(game);
        int max = game.getCountQuestionsInLap() * game.getNumberOfLaps();
        if (questionDtos.size() <= max) {
            game.getQuestions().addAll(customDataMapper.toQuestions(questionDtos, game));
            gameRepository.save(game);
        } else {
            throw new IllegalStateException("Questions limit exceeded");
        }
    }
}
