package ru.nsu.fit.cswd.intelli_games.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.cswd.intelli_games.domain.Team;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
