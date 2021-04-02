package ru.nsu.fit.cswd.intelli_games.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String name;
    @NotNull
    @Future
    private LocalDateTime dateStart;
    @NotNull
    @Min(1)
    private Integer secondsPerQuestion;
    @NotNull
    @Min(1)
    private Integer numberOfLaps;
    @NotNull
    @Min(1)
    private Integer minutesBetweenLaps;
    @NotNull
    @Min(1)
    private Integer countQuestionsInLap;
    private List<QuestionDto> questions;
}
