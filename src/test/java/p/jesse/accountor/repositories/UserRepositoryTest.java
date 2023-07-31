package p.jesse.accountor.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {UserRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"p.jesse.accountor.entities"})
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnUserByUsername() {
        //given
        String username = "Plymander";
        User user = new User("Jesse",
                "Plym",
                username,
                "asd123456",
                LocalDate.now(),
                Role.USER);
        userRepository.save(user);
        //when
        User userExists = userRepository.findByUsername(username).get();
        //then
        Assertions.assertThat(userExists).isNotNull();
    }

    @Test
    void shouldNotReturnUserByUsernameIfUsernameNotExist() {
        //given
        String username = "Plymander";
        //when
        Optional<User> userExists = userRepository.findByUsername(username);
        //then
        Assertions.assertThat(userExists).isEmpty();
    }
}