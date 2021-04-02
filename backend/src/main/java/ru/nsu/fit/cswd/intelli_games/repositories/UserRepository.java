package ru.nsu.fit.cswd.intelli_games.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.cswd.intelli_games.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
}
