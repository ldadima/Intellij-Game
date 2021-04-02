package ru.nsu.fit.cswd.intelli_games.config;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.nsu.fit.cswd.intelli_games.model.exceptions.NotFoundException;
import ru.nsu.fit.cswd.intelli_games.model.web.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn("Method argument validation detected errors");

        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        final FieldError error = ex.getBindingResult().getFieldErrors().get(0);
        final String message = error.getField() + ": " + error.getDefaultMessage();

        ErrorResponse response = new ErrorResponse(path, message, status, Instant.now());
        return ResponseEntity.status(status).headers(headers).body(response);
    }


    // Здесь можно добавлять исключения по мере необходимости
    @ExceptionHandler(value = {HibernateException.class})
    protected ResponseEntity<ErrorResponse> handleInternalErrors(RuntimeException ex, HttpServletRequest httpRequest) {
        logger.error("Exception thrown. Sending server error response", ex);
        final String path = httpRequest.getRequestURI();
        final String message = "Internal server error. Please contact administrator";
        ErrorResponse response = new ErrorResponse(path, message, HttpStatus.INTERNAL_SERVER_ERROR, Instant.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest httpRequest) {
        logger.warn("Not found exception thrown");
        final String path = httpRequest.getRequestURI();
        final String message = ex.getMessage();
        ErrorResponse response = new ErrorResponse(path, message, HttpStatus.NOT_FOUND, Instant.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgument(RuntimeException ex, HttpServletRequest httpRequest) {
        logger.warn("Illegal argument exception thrown");
        final String path = httpRequest.getRequestURI();
        final String message = ex.getMessage();
        ErrorResponse response = new ErrorResponse(path, message, HttpStatus.BAD_REQUEST, Instant.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
