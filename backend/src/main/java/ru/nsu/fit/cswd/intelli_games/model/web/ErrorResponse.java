package ru.nsu.fit.cswd.intelli_games.model.web;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public final class ErrorResponse {
    private final String path;
    private final String message;
    private final HttpStatus status;
    private final Instant timestamp;
}
