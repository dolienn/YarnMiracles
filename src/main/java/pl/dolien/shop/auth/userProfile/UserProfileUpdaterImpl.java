package pl.dolien.shop.auth.userProfile;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.password.PasswordChanger;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.auth.userProfile.dto.UserProfileDTO;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

@Service
@RequiredArgsConstructor
public class UserProfileUpdaterImpl implements UserProfileUpdater {

    private final UserService userService;
    private final PasswordChanger passwordChanger;

    @CacheEvict(cacheNames = {"connectedUser", "products", "productsByCategory",
                "productsByName", "productsWithFeedbacks", "feedbacksByProduct"}, allEntries = true)
    @Override
    public User updateUserProfile(UserProfileDTO request, Authentication connectedUser) {
        UserWithRoleDTO currentUser = userService.getUserByAuth(connectedUser);
        User userFromDB = userService.getUserById(currentUser.getId());

        passwordChanger.verifyPasswordMatch(request.getPassword(), userFromDB.getPassword());
        userService.assertEmailNotInUse(request.getEmail(), currentUser.getEmail());

        return updateAndSaveUser(request, userFromDB);
    }

    private User updateAndSaveUser(RegistrationDTO request, User currentUser) {
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        return userService.saveUser(currentUser);
    }
}

