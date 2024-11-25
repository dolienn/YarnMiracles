package pl.dolien.shop.auth.userProfile;

import org.springframework.security.core.Authentication;
import pl.dolien.shop.auth.userProfile.dto.UserProfileDTO;
import pl.dolien.shop.user.User;

public interface UserProfileUpdater {

    User updateUserProfile(UserProfileDTO request, Authentication connectedUser);
}

