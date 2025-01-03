package pl.dolien.shop.auth.password;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.exception.IncorrectPasswordException;
import pl.dolien.shop.exception.SamePasswordException;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PasswordChangerImpl implements PasswordChanger {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User changePassword(PasswordRequestDTO dto, Authentication connectedUser) {
        UserWithRoleDTO currentUser = userService.getUserByAuth(connectedUser);
        User userFromDB = userService.getUserById(currentUser.getId());

        verifyPasswordMatch(dto.getCurrentPassword(), userFromDB.getPassword());
        validateDifferentPasswords(dto.getCurrentPassword(), dto.getNewPassword());

        return updateUserPassword(dto.getNewPassword(), userFromDB);
    }

    public void verifyPasswordMatch(String inputPassword, String currentPassword) {
        if (!passwordEncoder.matches(inputPassword, currentPassword))
            throw new IncorrectPasswordException("The current password does not match the one entered in the form");
    }

    private void validateDifferentPasswords(String currentPassword, String newPassword) {
        if (Objects.equals(currentPassword, newPassword))
            throw new SamePasswordException("The current password and the new one are the same");
    }

    private User updateUserPassword(String newPassword, User currentUser) {
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        return userService.saveUser(currentUser);
    }
}

