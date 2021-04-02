package ru.nsu.fit.cswd.intelli_games.services;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.dto.UserDto;
import ru.nsu.fit.cswd.intelli_games.mapper.CustomDataMapper;
import ru.nsu.fit.cswd.intelli_games.model.RegistrationResult;
import ru.nsu.fit.cswd.intelli_games.repositories.UserRepository;
import ru.nsu.fit.cswd.intelli_games.services.model.TestUsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private CustomDataMapper customDataMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @EnumSource(value = TestUsers.class, names = {"DB_ERROR"}, mode = EnumSource.Mode.EXCLUDE)
    void register(TestUsers testUsers) {
        UserDto userDto = testUsers.getUserDto();
        User user = testUsers.getUser();
        when(customDataMapper.toDbUser(userDto, passwordEncoder)).thenReturn(user);
        if (testUsers == TestUsers.DB_ERROR) {
            when(userRepository.save(user)).thenThrow(new HibernateException(""));
        } else {
            when(userRepository.save(user)).thenReturn(user);
        }
        RegistrationResult result = userService.register(userDto);
        assertEquals(testUsers.getResult(), result);
    }
}