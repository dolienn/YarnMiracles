package pl.dolien.shop.auth.password;

import org.springframework.security.core.Authentication;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.user.User;

public interface PasswordChanger {

    User changePassword(PasswordRequestDTO dto, Authentication connectedUser);

    void verifyPasswordMatch(String inputPassword, String currentPassword);
}

