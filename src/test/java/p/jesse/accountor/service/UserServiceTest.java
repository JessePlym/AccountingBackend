package p.jesse.accountor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // argumentcaptor

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
    void shouldGetAllUsers() {
        // when
        userService.getUsers();
        // then
        Mockito.verify(userRepository).findAll();
    }

    @Test
    @Disabled
    void updateUserDetails() {
    }

    @Test
    @Disabled
    void updatePassword() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }


    @Test
    void shouldLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(user));
        assertSame(user, userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.any());
    }


    @Test
    void shouldThrowWhenUsernameNotFound() throws UsernameNotFoundException {
        when(userRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.any());
    }

}