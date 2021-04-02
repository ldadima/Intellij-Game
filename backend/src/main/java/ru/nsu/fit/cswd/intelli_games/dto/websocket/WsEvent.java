package ru.nsu.fit.cswd.intelli_games.dto.websocket;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

@Data
public class WsEvent {
    private final ObjectType objectType;
    private final EventType eventType;

    @JsonRawValue
    private final String body;
}
