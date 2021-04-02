package ru.nsu.fit.cswd.intelli_games.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.nsu.fit.cswd.intelli_games.domain.Question;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
