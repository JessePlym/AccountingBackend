package p.jesse.accountor.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.UserRepository;

@Component
public class AuthChecker {

    public User extractUser(Authentication authentication, UserRepository userRepository) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public boolean isUserNotAuthenticated(String username, Authentication authentication) {
        return !username.equals(authentication.getName());
    }
}
