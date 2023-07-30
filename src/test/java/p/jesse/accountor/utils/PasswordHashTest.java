package p.jesse.accountor.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = {PasswordHash.class})
@ExtendWith(SpringExtension.class)
class PasswordHashTest {

    @Autowired
    private PasswordHash passwordHash;

    @Test
    void shouldEncryptPassword() {
        // given
        String password = "abc123!90";
        //when
        String encryptedPassword = passwordHash.encryptPassword(password);
        //then
        Assertions.assertThat(encryptedPassword).startsWith("$");
        Assertions.assertThat(encryptedPassword.length()).isEqualTo(60);
    }

    @Test
    void shouldCheckThatDecryptedPasswordCorrespondsInput() {
        // given
        String input = "abc123!90";
        String hashedPassword = "$2a$12$o2a7hfRW8ypfEzFe80u/0OJ6cx7ch9xElxkcEcPH6ybImRw3Wqk46";
        // when
        boolean isCorrect = passwordHash.checkPassword(hashedPassword, input);
        // then
        Assertions.assertThat(isCorrect).isTrue();
    }
}