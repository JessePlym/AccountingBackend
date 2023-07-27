package p.jesse.accountor.utils;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Component
public class PasswordHash {

    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String hash, String password) {
        return BCrypt.checkpw(password, hash);
    }
}
