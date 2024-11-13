package pl.dolien.shop.auth.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.password.PasswordChanger;
import pl.dolien.shop.auth.registration.RegistrationDTO;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class UserInfoUpdater {

    private final UserService userService;
    private final PasswordChanger passwordChanger;

    public User updateUserInformation(RegistrationDTO request, Authentication connectedUser) {
        UserDTO currentUser = userService.getUserDTOByAuth(connectedUser);
        User userFromDB = userService.getUserById(currentUser.getId());

        passwordChanger.verifyPasswordMatch(request.getPassword(), userFromDB.getPassword());
        userService.assertEmailNotInUse(request.getEmail(), currentUser.getEmail());

        return applyProfileUpdates(request, userFromDB);
    }

    private User applyProfileUpdates(RegistrationDTO request, User currentUser) {
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        return userService.saveUser(currentUser);
    }
}

