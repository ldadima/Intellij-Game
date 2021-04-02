package ru.nsu.fit.cswd.intelli_games.model.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.nsu.fit.cswd.intelli_games.dto.websocket.EventType;
import ru.nsu.fit.cswd.intelli_games.dto.websocket.ObjectType;
import ru.nsu.fit.cswd.intelli_games.dto.websocket.WsEvent;

import java.util.function.BiConsumer;

@AllArgsConstructor
public class WsSender {
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType) {
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writer();

        return (EventType eventType, T payload) -> {
            String value;

            try {
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            template.convertAndSend(
                    "/topic/activity",
                    new WsEvent(objectType, eventType, value)
            );
        };
    }
}
