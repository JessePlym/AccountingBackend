package p.jesse.accountor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.NewPasswordRequest;
import p.jesse.accountor.records.UserUpdateRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHash passwordHash;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordHash);
    }


    @Test
    void shouldUpdatePassword() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(existingUser));
        when(passwordHash.encryptPassword(Mockito.anyString())).thenAnswer(invocationOnMock -> {
            String rawPassword = invocationOnMock.getArgument(0);
            return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        });
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest("new_password", "new_password");
        ResponseEntity<String> actualUpdatePasswordResult = userService.updatePassword(newPasswordRequest, new BearerTokenAuthenticationToken("ABC123"));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(BCrypt.checkpw(newPasswordRequest.password(), capturedUser.getPassword())).isTrue();
        assertThat(actualUpdatePasswordResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDontMatch() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(new User()));
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest("new_password", "old_password");
        assertThrows(IllegalStateException.class,
                () -> userService.updatePassword(newPasswordRequest, new BearerTokenAuthenticationToken("ABC123")),
                "Passwords do not match!");
    }

    @Test
    void shouldThrowWhenUsernameNotFoundWhenUpdatingPassword() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userService.updatePassword(null, new BearerTokenAuthenticationToken("ABC123")),
                "user not found");
        verify(userRepository).findByUsername(Mockito.any());
    }

    @Test
    void shouldGetAllUsers() {
        userService.getUsers();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void shouldDeleteUserWith204StatusCode() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(existingUser));
        ResponseEntity<Object> actualResult = userService.deleteUser(existingUser.getId(), new BearerTokenAuthenticationToken(existingUser.getUsername()));

        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userRepository).deleteById(Mockito.any());
    }

    @Test
    void shouldReturn403StatusCodeIfNotCorrectUserAuthenticated() {
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));
        ResponseEntity<Object> actualResult = userService.deleteUser(null, new BearerTokenAuthenticationToken("ABC123"));

        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturn400IfBadUserIdWhenDeletingUser() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity<Object> actualResult = userService.deleteUser(-1L, new BearerTokenAuthenticationToken(existingUser.getUsername()));

        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResult.getBody()).isEqualTo("User with given id does not exist");
    }


    @Test
    void shouldLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));
        assertSame(user, userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.any());
    }

    @Test
    void shouldUpdateUserDetails() {
        when(userRepository.save(Mockito.any())).thenReturn(new User());
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(new User()));
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("John", "Smith");

        ResponseEntity<String> actualUpdateUserDetailsResult = userService.updateUserDetails(userUpdateRequest,
                new BearerTokenAuthenticationToken("ABC123"));
        assertNull(actualUpdateUserDetailsResult.getBody());
        assertEquals(200, actualUpdateUserDetailsResult.getStatusCodeValue());
        verify(userRepository).save(Mockito.any());
        verify(userRepository).findByUsername(Mockito.any());
    }

    @Test
    void shouldThrowWhenUsernameNotFound() {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("someuser"));
        verify(userRepository).findByUsername(Mockito.any());
    }

}