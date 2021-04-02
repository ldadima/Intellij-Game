package ru.nsu.fit.cswd.intelli_games.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionDto {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String textQuestion;
    @Null
    @Min(value = 1)
    private Integer numberInGame;
    @NotNull
    @Size(min = 1)
    private String answer;
    private Integer numberLap;
}
