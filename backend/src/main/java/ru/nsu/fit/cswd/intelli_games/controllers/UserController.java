package ru.nsu.fit.cswd.intelli_games.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.nsu.fit.cswd.intelli_games.dto.PlayerDto;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;
import ru.nsu.fit.cswd.intelli_games.model.web.Response;
import ru.nsu.fit.cswd.intelli_games.services.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto dto) {
        return switch (userService.register(dto)) {
            case SUCCESS -> Response.created();
            case PASSWORDS_DONT_MATCH -> Response.badRequest("Passwords don't match");
            case DB_ERROR -> Response.badRequest("Bad credentials");
        };
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDto> currentUser() {
        return Response.ok(userService.currentUser());
    }
}
