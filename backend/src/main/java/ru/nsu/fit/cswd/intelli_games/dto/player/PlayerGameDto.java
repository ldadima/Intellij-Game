package ru.nsu.fit.cswd.intelli_games.dto.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.fit.cswd.intelli_games.dto.GameDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PlayerGameDto extends GameDto {
    private Boolean participation;
}
