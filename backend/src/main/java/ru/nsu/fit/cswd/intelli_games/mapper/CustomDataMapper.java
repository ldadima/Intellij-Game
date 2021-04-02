package ru.nsu.fit.cswd.intelli_games.mapper;

import jdk.jfr.Name;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nsu.fit.cswd.intelli_games.domain.Game;
import ru.nsu.fit.cswd.intelli_games.domain.Question;
import ru.nsu.fit.cswd.intelli_games.domain.Team;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;
import ru.nsu.fit.cswd.intelli_games.dto.PlayerDto;
import ru.nsu.fit.cswd.intelli_games.dto.QuestionDto;
import ru.nsu.fit.cswd.intelli_games.dto.TeamDto;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CustomDataMapper {

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    protected abstract User toUser(UserDto userDto);


    public User toDbUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        User user = toUser(userDto);
        if (user == null)
            return null;
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    @Mapping(target = "numberLap", ignore = true)
    protected abstract QuestionDto toQuestionDto(Question question);

    protected QuestionDto toQuestionDtoCustom(Question question, Game game) {
        QuestionDto questionDto = toQuestionDto(question);
        if (questionDto == null)
            return null;
        questionDto.setNumberLap((questionDto.getNumberInGame() - 1)/ game.getCountQuestionsInLap() + 1);
        return questionDto;
    }

    @Mapping(target = "id", source = "questionDto.id")
    @Mapping(target = "game", source = "gameDb")
    @Mapping(target = "numberInGame", ignore = true)
    protected abstract Question toQuestion(QuestionDto questionDto, Game gameDb, @MappingTarget Question question);

    public List<Question> toQuestions(List<QuestionDto> questions, Game game){
        if(questions == null){
            return null;
        }
        List<Question> questionsList = new ArrayList<>();
        for(QuestionDto questionDto: questions){
            Question question = toQuestion(questionDto, game, new Question());
            question.setNumberInGame(questionsList.size() + 1);
            questionsList.add(question);
        }
        return questionsList;
    }

    @Mapping(target = "teams", ignore = true)
    public abstract Game toGameEntity(GameDto gameDto, @MappingTarget Game game);

    @Mapping(target = "questions", ignore = true)
    public abstract GameDto toGameDto(Game game, @MappingTarget GameDto gameDto);

    public GameDto toGameDtoCustom(Game game, GameDto gameDto) {
        toGameDto(game, gameDto);
        if (game.getQuestions() == null) {
            gameDto.setQuestions(null);
            return gameDto;
        }
        List<QuestionDto> questionDtos = new ArrayList<>();
        if (gameDto.getQuestions() == null) {
            for(Question question: game.getQuestions()){
                questionDtos.add(toQuestionDtoCustom(question, game));
            }
            gameDto.setQuestions(questionDtos);
        } else {
            for(Question question: game.getQuestions()){
                questionDtos.add(toQuestionDtoCustom(question, game));
            }
            gameDto.getQuestions().clear();
            gameDto.getQuestions().addAll(questionDtos);
        }
        return gameDto;
    }

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "game", ignore = true)
    public abstract Team toTeamEntity(TeamDto teamDto);


    public abstract TeamDto toTeamDto(Team team);

    public abstract List<TeamDto> toTeamDtoList(Iterable<Team> teams);

    public abstract List<PlayerDto> toPlayerDtoList(Iterable<User> users);

    public abstract PlayerDto toPlayerDto(User user);
}
