package ru.nsu.fit.cswd.intelli_games.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.nsu.fit.cswd.intelli_games.domain.Game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends PagingAndSortingRepository<Game, Long> {
    Optional<Game> findByDateStart(LocalDateTime dateTime);
    Page<Game> findGamesByDateStartAfter(Pageable pageable, LocalDateTime dateTime);
}
