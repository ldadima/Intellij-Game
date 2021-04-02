package ru.nsu.fit.cswd.intelli_games.services;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import ru.nsu.fit.cswd.intelli_games.domain.Game;
import ru.nsu.fit.cswd.intelli_games.domain.Team;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.dto.admin.AdminGameDto;
import ru.nsu.fit.cswd.intelli_games.dto.player.PlayerGameDto;
import ru.nsu.fit.cswd.intelli_games.mapper.CustomDataMapper;
import ru.nsu.fit.cswd.intelli_games.model.OperationResult;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;
import ru.nsu.fit.cswd.intelli_games.model.exceptions.NotFoundException;
import ru.nsu.fit.cswd.intelli_games.repositories.GameRepository;
import ru.nsu.fit.cswd.intelli_games.services.model.TestGames;
import ru.nsu.fit.cswd.intelli_games.services.model.TestUsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CustomDataMapper customDataMapper;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @EnumSource(value = TestGames.class, names = {"NOT_FOUND", "DB_ERROR"}, mode = EnumSource.Mode.EXCLUDE)
    void create(TestGames testGames) {
        Game game = testGames.getGame();
        GameDto gameDto = testGames.getGameDto();
        doAnswer(invocationOnMock -> {
            Game arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(arg2, game);
            return null;
        }).when(customDataMapper).toGameEntity(gameDto, new Game());
        switch (testGames) {
            case SUCCESS -> when(gameRepository.save(game)).thenReturn(game);
            case DB_ERROR -> when(gameRepository.save(game)).thenThrow(new HibernateException(""));
        }
        Long id = gameService.create(gameDto);
        assertEquals(testGames.getGame().getId(), id);
        verify(gameRepository).save(game);
    }

    @ParameterizedTest
    @EnumSource(value = TestGames.class, names = {"DB_ERROR"}, mode = EnumSource.Mode.EXCLUDE)
    void update(TestGames testGames) {
        Game game = testGames.getGame();
        GameDto gameDto = testGames.getGameDto();
        doAnswer(invocationOnMock -> {
            Game arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(game, arg2);
            return null;
        }).when(customDataMapper).toGameEntity(gameDto, new Game());
        switch (testGames) {
            case SUCCESS -> {
                when(gameRepository.findById(gameDto.getId())).thenReturn(Optional.of(game));
                when(gameRepository.save(game)).thenReturn(game);
                OperationResult result = gameService.update(gameDto);
                assertEquals(testGames.getResult(), result);
            }
            case DB_ERROR -> {
                when(gameRepository.findById(gameDto.getId())).thenReturn(Optional.of(game));
                when(gameRepository.save(game)).thenThrow(new HibernateException(""));
            }
            case NOT_FOUND -> {
                when(gameRepository.findById(gameDto.getId())).thenReturn(Optional.empty());
                assertThrows(NotFoundException.class ,() -> gameService.update(gameDto));
            }
        }

        if (!TestGames.NOT_FOUND.equals(testGames)) {
            verify(gameRepository).save(game);
        }
    }

    @Test
    void findAll() {
        Authentication authentication = mock(Authentication.class);
        User user = TestUsers.SUCCESS.getUser();
        Team team1 = new Team();
        Team team2 = new Team();
        Team team3 = new Team();
        Game game1 = new Game(1L, null, null, null, null, null, null,
                new ArrayList<>(List.of(team1, team2)), Collections.emptyList());
        team1.setGame(game1);
        team2.setGame(game1);
        Game game2 = new Game(2L, null, null, null, null, null, null,
                new ArrayList<>(List.of(team3)), Collections.emptyList());
        user.setTeam(team1);
        Page<Game> gamePage = new PageImpl<>(List.of(game1, game2));
        when(gameRepository.findAll(PageRequest.of(0, 5, Sort.by("dateStart").ascending()))).thenReturn(gamePage);
        when(authentication.getPrincipal()).thenReturn(user);
        GameDto pGameDto1 = new PlayerGameDto(true);
        pGameDto1.setId(1L);
        GameDto pGameDto2 = new PlayerGameDto(false);
        pGameDto2.setId(2L);

        doAnswer(invocationOnMock -> {
            GameDto arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(pGameDto1, arg2);
            return null;
        }).when(customDataMapper).toGameDtoCustom(game1, new PlayerGameDto());
        doAnswer(invocationOnMock -> {
            GameDto arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(pGameDto2, arg2);
            return null;
        }).when(customDataMapper).toGameDtoCustom(game2, new PlayerGameDto());

        List<GameDto> actualGameDtos = gameService.findAll(0, 5, (User) authentication.getPrincipal()).toList();
        assertEquals(List.of(pGameDto1,pGameDto2), actualGameDtos);

        user.setRole(UserRole.Admin);
        GameDto aGameDto1 = new AdminGameDto(2);
        aGameDto1.setId(1L);
        GameDto aGameDto2 = new AdminGameDto(1);
        aGameDto2.setId(2L);

        doAnswer(invocationOnMock -> {
            GameDto arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(aGameDto1, arg2);
            return null;
        }).when(customDataMapper).toGameDtoCustom(game1, new AdminGameDto());
        doAnswer(invocationOnMock -> {
            GameDto arg2 = invocationOnMock.getArgument(1);
            BeanUtils.copyProperties(aGameDto2, arg2);
            return null;
        }).when(customDataMapper).toGameDtoCustom(game2, new AdminGameDto());

        actualGameDtos = gameService.findAll(0, 5, (User) authentication.getPrincipal()).toList();
        assertEquals(List.of(aGameDto1, aGameDto2), actualGameDtos);
    }
}