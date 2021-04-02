package ru.nsu.fit.cswd.intelli_games.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nsu.fit.cswd.intelli_games.dto.PlayerDto;
import ru.nsu.fit.cswd.intelli_games.dto.TeamDto;
import ru.nsu.fit.cswd.intelli_games.dto.websocket.EventType;
import ru.nsu.fit.cswd.intelli_games.dto.websocket.ObjectType;
import ru.nsu.fit.cswd.intelli_games.model.web.Response;
import ru.nsu.fit.cswd.intelli_games.model.web.WsSender;
import ru.nsu.fit.cswd.intelli_games.services.TeamService;

import javax.validation.Valid;
import java.util.List;
import java.util.function.BiConsumer;

@Controller
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;
    private final BiConsumer<EventType, Object> sender;


    public TeamController(TeamService service, WsSender wsSender) {
        this.teamService = service;
        this.sender = wsSender.getSender(ObjectType.TEAM);
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAll() {
        return Response.ok(teamService.findAll());
    }

    @GetMapping("/teammates")
    @PreAuthorize("hasAnyAuthority('Captain', 'Player')")
    public ResponseEntity<List<PlayerDto>> showTeammates() {
        return Response.ok(teamService.showTeammates());
    }

    @GetMapping("/name")
    @PreAuthorize("hasAnyAuthority('Captain', 'Player')")
    public ResponseEntity<String> currentTeamName() {
        return Response.ok(teamService.currentTeamName());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Player')")
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamDto teamDto) {
        final TeamDto team = teamService.createTeam(teamDto);
        sender.accept(EventType.CREATE, team);
        return Response.created(team);
    }

    @PutMapping("/{team_id}")
    @PreAuthorize("hasAnyAuthority('Player')")
    public ResponseEntity<List<PlayerDto>> joinToTeam(@PathVariable("team_id") Long teamId) {
        final List<PlayerDto> teammates = teamService.joinToTeam(teamId);
        sender.accept(EventType.UPDATE, teammates);
        return Response.ok(teammates);
    }

    @PutMapping("delete/{user_id}")
    @PreAuthorize("hasAuthority('Captain')")
    public ResponseEntity<List<PlayerDto>> deleteTeammate(@PathVariable("user_id") Long userId) {
        final List<PlayerDto> teammates = teamService.deleteTeammate(userId);
        sender.accept(EventType.DELETE, null);
        return Response.ok(teammates);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('Captain')")
    public ResponseEntity<?> deleteTeam() {
        teamService.deleteTeam();
        sender.accept(EventType.DELETE, null);
        return Response.ok();
    }

    @PutMapping("/leave")
    @PreAuthorize("hasAuthority('Player')")
    public ResponseEntity<?> leaveTeam() {
        teamService.leaveTeam();
        sender.accept(EventType.DELETE, null);
        return Response.ok();
    }
}
