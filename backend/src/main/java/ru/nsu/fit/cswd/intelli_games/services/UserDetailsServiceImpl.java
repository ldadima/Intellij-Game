package ru.nsu.fit.cswd.intelli_games.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.fit.cswd.intelli_games.domain.User;
import ru.nsu.fit.cswd.intelli_games.model.exceptions.NotFoundException;
import ru.nsu.fit.cswd.intelli_games.repositories.UserRepository;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private static User getPrincipalCopy() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userCopy = new User();
        BeanUtils.copyProperties(user, userCopy);
        return userCopy;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final Optional<User> userByLogin = userRepository.findUserByLogin(login);
        return userByLogin
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void updatePrincipal(Consumer<User> operation) {
        User user = getPrincipalCopy();
        operation.accept(user);
        user = userRepository.save(user);

        reloadContext(user);
    }

    public User reloadPrincipal() {
        final User user = userRepository.findById(getPrincipalCopy().getId())
                .orElseThrow(() -> new NotFoundException("Current user is missing in database"));
        reloadContext(user);
        return user;
    }

    private void reloadContext(User updatedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
