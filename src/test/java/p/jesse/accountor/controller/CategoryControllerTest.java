package p.jesse.accountor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.repositories.CategoryRepository;
import p.jesse.accountor.service.JwtService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    void shouldReturnAllCategories() throws Exception{
        String token = jwtService.generateToken(new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER));

        mockMvc.perform(get("/api/categories")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddNewCategory() throws Exception{
        String token = jwtService.generateToken(new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER));
        Category category = new Category("hobby");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        String token = jwtService.generateToken(new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER));
        Category category = new Category("hobby");
        categoryRepository.save(category);
        Long id = categoryRepository.findByName("hobby").get().getId();

        mockMvc.perform(delete("/api/categories/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}