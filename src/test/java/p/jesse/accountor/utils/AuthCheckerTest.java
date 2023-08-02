package p.jesse.accountor.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.repositories.UserRepository;

@ContextConfiguration(classes = {AuthChecker.class})
@ExtendWith(SpringExtension.class)
class AuthCheckerTest {
    @Autowired
    private AuthChecker authChecker;

    @MockBean
    private UserRepository userRepository;


    @Test
    void shouldExtractUser() {
        BearerTokenAuthenticationToken authentication = new BearerTokenAuthenticationToken("ABC123");
        UserRepository userRepository2 = mock(UserRepository.class);
        User user = new User();
        when(userRepository2.findByUsername(Mockito.<String>any())).thenReturn(Optional.of(user));
        assertSame(user, authChecker.extractUser(authentication, userRepository2));
        verify(userRepository2).findByUsername(Mockito.<String>any());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        BearerTokenAuthenticationToken authentication = new BearerTokenAuthenticationToken("ABC123");
        UserRepository userRepository2 = mock(UserRepository.class);
        when(userRepository2.findByUsername(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> authChecker.extractUser(authentication, userRepository2));
        verify(userRepository2).findByUsername(Mockito.<String>any());
    }

    @Test
    void shouldCheckIfCorrectUserIsAuthenticated() {
        User user = new User("J", "P", "jp", "password", LocalDate.now(), Role.USER);
        BearerTokenAuthenticationToken authentication = new BearerTokenAuthenticationToken(user.getUsername());
        boolean actualResult = authChecker.isUserNotAuthenticated(user.getUsername(), authentication);
        assertThat(actualResult).isFalse();
    }
}

