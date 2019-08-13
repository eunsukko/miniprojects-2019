package techcourse.fakebook.utils.encryptor;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptorEncryptor implements Encryptor {
    @Override
    public String encrypt(String data) {
        return BCrypt.hashpw(data, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String data, String encrypted) {
        return BCrypt.checkpw(data, encrypted);
    }
}
