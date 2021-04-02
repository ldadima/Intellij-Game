package ru.nsu.fit.cswd.intelli_games.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.cswd.intelli_games.domain.Team;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.PlayerDto;
import ru.nsu.fit.cswd.intelli_games.dto.TeamDto;
import ru.nsu.fit.cswd.intelli_games.mapper.CustomDataMapper;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;
import ru.nsu.fit.cswd.intelli_games.model.exceptions.NotFoundException;
import ru.nsu.fit.cswd.intelli_games.repositories.TeamRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CustomDataMapper customDataMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public TeamDto createTeam(TeamDto team) {
        if(teamRepository.findByName(team.getName()).isPresent()){
            throw new IllegalArgumentException("Название команды занято");
        }
        final Team savedTeam = teamRepository.save(customDataMapper.toTeamEntity(team));
        userDetailsService.updatePrincipal(u -> {
            u.setRole(UserRole.Captain);
            savedTeam.setUsers(new ArrayList<>(List.of(u)));
            u.setTeam(savedTeam);
        });

        return customDataMapper.toTeamDto(savedTeam);
    }

    public List<PlayerDto> joinToTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Команды не существует"));

        userDetailsService.updatePrincipal(u -> {
            u.setTeam(team);
            team.getUsers().add(u);
        });
        teamRepository.save(team);


        return showTeammates();
    }

    public List<PlayerDto> showTeammates() {
        User actualUser = userDetailsService.reloadPrincipal();
        if (actualUser.getTeam() == null) {
            return Collections.emptyList();
        }
        final Team storedTeam = teamRepository.findById(actualUser.getTeam().getId())
                .orElseThrow(() -> new NotFoundException("Team not found for user"));
        List<User> users = storedTeam.getUsers();
        users.sort(Comparator.comparing(user -> user.getRole().name()));
        return customDataMapper.toPlayerDtoList(users);
    }

    public List<PlayerDto> deleteTeammate(Long userId) {
        User actualUser = userDetailsService.reloadPrincipal();
        Team team = actualUser.getTeam();
        if (!team.getUsers().removeIf(u -> u.getId().equals(userId))) {
            throw new NotFoundException("Not found user with id " + userId);
        }
        teamRepository.save(team);

        userDetailsService.updatePrincipal(u -> u.setTeam(team));

        return showTeammates();
    }

    public void deleteTeam() {
        User actualUser = userDetailsService.reloadPrincipal();
        teamRepository.delete(actualUser.getTeam());

        userDetailsService.updatePrincipal((u) -> {
            u.setTeam(null);
            u.setRole(UserRole.Player);
        });
    }

    public List<TeamDto> findAll() {
        return customDataMapper.toTeamDtoList(teamRepository.findAll());
    }

    public void leaveTeam() {
        final User user = userDetailsService.reloadPrincipal();
        if (user.getTeam() == null) {
            return;
        }

        userDetailsService.updatePrincipal(u -> u.setTeam(null));
    }

    public String currentTeamName() {
        User user = userDetailsService.reloadPrincipal();
        if(user.getTeam() == null){
            throw new IllegalStateException(String.format("User %s not on team", user.getLogin()));
        }
        return user.getTeam().getName();
    }
}
