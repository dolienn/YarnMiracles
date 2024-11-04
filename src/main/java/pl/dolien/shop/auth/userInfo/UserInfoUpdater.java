package pl.dolien.shop.auth.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.password.PasswordChanger;
import pl.dolien.shop.auth.registration.RegistrationDTO;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

@Service
@RequiredArgsConstructor
public class UserInfoUpdater {

    private final UserService userService;
    private final PasswordChanger passwordChanger;

    public void updateUserInformation(RegistrationDTO request, Authentication connectedUser) {
        User currentUser = userService.getUserByAuth(connectedUser);

        passwordChanger.verifyPasswordMatch(request.getPassword(), currentUser.getPassword());
        validateEmailUniqueness(request.getEmail(), currentUser.getEmail());

        applyProfileUpdates(request, currentUser);
    }

    private void validateEmailUniqueness(String newEmail, String currentEmail) {
        if(isEmailTakenByAnotherUser(newEmail, currentEmail)) {
            throw new EmailAlreadyExistsException("There is a user with the same email");
        }
    }

    private void applyProfileUpdates(RegistrationDTO request, User currentUser) {
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        userService.saveUser(currentUser);
    }

    private boolean isEmailTakenByAnotherUser(String newEmail, String currentEmail) {
        return userService.isUserExists(newEmail) && !newEmail.equals(currentEmail);
    }
}

