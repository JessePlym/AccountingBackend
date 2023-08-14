package p.jesse.accountor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordHash passwordHash;

    @BeforeEach
    void createTestUser() {
        userRepository.deleteAll();
        userRepository.save(new User("Jesse", "Plym", "jplym", passwordHash.encryptPassword("_password"), LocalDate.now(), Role.USER));
    }
    @Test
    void shouldRegister() throws Exception {
        RegisterRequest request = new RegisterRequest("Jesse", "Plym", "username", "password", "password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAuthenticateAndReturnToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("jplym", "_password");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

