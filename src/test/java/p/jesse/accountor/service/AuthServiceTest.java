package p.jesse.accountor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import javax.security.auth.login.CredentialException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AuthService.class})
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHash passwordHash;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordHash, jwtService);
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest("Jesse", "Plym", "jplym", "password", "password");
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        ResponseEntity<String> actualRequest = authService.register(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getUsername()).isEqualTo("jplym");
        assertThat(actualRequest.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldThrowAndReturn400IfUsernameExists() {
        RegisterRequest request = new RegisterRequest("Jesse", "Plym", "jplym", "password", "password");
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(existingUser));

        assertThrows(DataIntegrityViolationException.class,
                () -> authService.register(request),
                "Username already exists!");
    }

    @Test
    void shouldThrowAndReturn400IfPasswordsDontMatch() {
        RegisterRequest request = new RegisterRequest("Jesse", "Plym", "jplym", "password", "passwordCheck");
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> authService.register(request),
                "Passwords do not match!");
    }

    @Test
    void shouldAuthenticateUserAndReturnTokenThroughHeaders() throws CredentialException {
        AuthenticationRequest request = new AuthenticationRequest("jplym", "password");
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(new User()));
        when(passwordHash.checkPassword(Mockito.any(), Mockito.any())).thenReturn(true);
        when(jwtService.generateToken(Mockito.any())).thenReturn("ABC123");

        ResponseEntity<String> actualRequest = authService.authenticate(request);

        assertThat(actualRequest.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualRequest.getHeaders().size()).isEqualTo(1);

        verify(userRepository, atLeast(1)).findByUsername(Mockito.any());
        verify(passwordHash).checkPassword(Mockito.any(), Mockito.any());
        verify(jwtService).generateToken(Mockito.any());
    }

    @Test
    void shouldThrowIfUsernameDoesNotExistWhenAuthenticating() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(CredentialException.class,
                () -> authService.authenticate(new AuthenticationRequest("", "")),
                "Username does not exist!");

        verify(userRepository, atLeast(1)).findByUsername(Mockito.any());
    }

    @Test
    void shouldThrowIfIncorrectCredentialsWhenAuthenticating() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(new User()));
        when(passwordHash.checkPassword(Mockito.any(), Mockito.any())).thenReturn(false);

        assertThrows(CredentialException.class,
                () -> authService.authenticate(new AuthenticationRequest("", "")),
                "Username or password incorrect!");

        verify(userRepository, atLeast(1)).findByUsername(Mockito.any());
        verify(passwordHash).checkPassword(Mockito.any(), Mockito.any());
    }
}