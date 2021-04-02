package ru.nsu.fit.cswd.intelli_games.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.dto.QuestionDto;
import ru.nsu.fit.cswd.intelli_games.model.web.Response;
import ru.nsu.fit.cswd.intelli_games.services.GameService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> create(@RequestBody @Valid GameDto gameDto) {
        return Response.created(gameService.create(gameDto));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> update(@RequestBody @Valid GameDto gameDto) {
        return switch (gameService.update(gameDto)) {
            case SUCCESS -> Response.ok();
            case DB_ERROR -> Response.badRequest("Bad credentials");
            case NOT_FOUND -> Response.notFound("Not found game with id - " + gameDto.getId());
        };
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Page<GameDto>> findAll(@AuthenticationPrincipal User user, Integer page, Integer size) {
        return Response.ok(gameService.findAll(page, size, user));
    }

    @PutMapping("/participate")
    @PreAuthorize("hasAuthority('Captain')")
    public ResponseEntity<?> participateInGame(@RequestBody GameDto gameDto) {
        return switch (gameService.participate(gameDto)) {
            case SUCCESS -> Response.ok();
            case DB_ERROR -> Response.badRequest("Bad credentials");
            case NOT_FOUND -> Response.notFound("Not found game with id - " + gameDto.getId());
        };
    }

    @PutMapping("/cancelParticipate")
    @PreAuthorize("hasAuthority('Captain')")
    public ResponseEntity<?> cancelParticipateInGame() {
        return switch (gameService.cancelParticipate()) {
            case SUCCESS -> Response.ok();
            case DB_ERROR -> Response.badRequest("Bad credentials");
            case NOT_FOUND -> Response.notFound("Not found game");
        };
    }

    @GetMapping("/{game_id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<GameDto> gameById(@PathVariable("game_id") long gameId) {
        GameDto gameDto = gameService.findGame(gameId);
        return Response.ok(gameDto);
    }

    @DeleteMapping("/{game_id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> deleteGameById(@PathVariable("game_id") long gameId) {
        gameService.deleteGame(gameId);
        return Response.ok();
    }

    @PutMapping("/{game_id}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> updateQuestionsInGame(@PathVariable("game_id") long gameId, @RequestBody List<@Valid QuestionDto> questions) {
        gameService.updateQuestions(gameId, questions);
        return Response.ok();
    }
}
