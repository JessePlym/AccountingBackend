package p.jesse.accountor.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.repositories.UserRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    /*@BeforeAll
    static void createUser() {

    }*/

    @Test
    void shouldReturnTokenWhenUserAuthenticates() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("Plymander", "jplym1996");

        assertEquals(request.username(), "Plymander");
    }
}