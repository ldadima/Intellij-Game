package ru.nsu.fit.cswd.intelli_games.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.cswd.intelli_games.dto.PlayerDto;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;
import ru.nsu.fit.cswd.intelli_games.mapper.CustomDataMapper;
import ru.nsu.fit.cswd.intelli_games.model.RegistrationResult;
import ru.nsu.fit.cswd.intelli_games.repositories.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomDataMapper customDataMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public RegistrationResult register(UserDto dto) {
        if (userRepository.findUserByLogin(dto.getLogin()).isPresent()) {
            throw new IllegalArgumentException(String.format("User with login '%s' already exists", dto.getLogin()));
        }
        if (!Objects.equals(dto.getPassword(), dto.getPasswordConfirmation())) {
            return RegistrationResult.PASSWORDS_DONT_MATCH;
        }
        userRepository.save(customDataMapper.toDbUser(dto, passwordEncoder));
        return RegistrationResult.SUCCESS;
    }

    public PlayerDto currentUser() {
        return customDataMapper.toPlayerDto(userDetailsService.reloadPrincipal());
    }
}
