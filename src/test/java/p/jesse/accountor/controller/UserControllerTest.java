package p.jesse.accountor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.NewPasswordRequest;
import p.jesse.accountor.records.UserUpdateRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.service.JwtService;
import p.jesse.accountor.service.UserService;
import p.jesse.accountor.utils.PasswordHash;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordHash passwordHash;
    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        userRepository.save(user);
    }


    @Test
    void shouldReturnAllUsers() throws Exception {
        String token = jwtService.generateToken(new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.ADMIN));

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUserDetails() throws Exception{
        Optional<User> user = userRepository.findByUsername("jplym");
        String token = jwtService.generateToken(user.get());

        UserUpdateRequest request = new UserUpdateRequest("Jessie", "Blym");

        mockMvc.perform(put("/api/users/current/update-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdatePassword() throws Exception{
        Optional<User> user = userRepository.findByUsername("jplym");
        String token = jwtService.generateToken(user.get());

        NewPasswordRequest request = new NewPasswordRequest("new_password", "new_password");

        mockMvc.perform(put("/api/users/current/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        Optional<User> user = userRepository.findByUsername("jplym");
        String token = jwtService.generateToken(user.get());
        Long id = user.get().getId();

        mockMvc.perform(delete("/api/users/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}