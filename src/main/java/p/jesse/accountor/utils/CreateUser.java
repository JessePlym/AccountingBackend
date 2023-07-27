package p.jesse.accountor.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static p.jesse.accountor.enums.Role.ROLE_ADMIN;
import static p.jesse.accountor.enums.Role.ROLE_USER;

@Component
public class CreateUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHash passwordHash;

    public void createTestUsers() {
        userRepository.deleteAll();

        User testUser1 = new User("Jesse", "Plym", "Plymander", passwordHash.encryptPassword("jplym96"), LocalDate.now(), ROLE_USER);
        User testUser2 = new User("Admin", "Admin", "admin", passwordHash.encryptPassword("verysecret"), LocalDate.now(), ROLE_ADMIN);

        userRepository.saveAll(List.of(testUser1, testUser2));
    }
}
