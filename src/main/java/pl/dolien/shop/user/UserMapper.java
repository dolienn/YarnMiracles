package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User updateUserFromDto(User user, UserDTO userDto) {
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setAccountLocked(userDto.isAccountLocked());
        user.setDateOfBirth(userDto.getDateOfBirth());

        if (userDto.getPassword() != null) {
            user.setPassword(encodePassword(userDto.getPassword()));
        }

        return user;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
