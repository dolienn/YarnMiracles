package pl.dolien.shop.auth.registration;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.email.EmailTemplateName;
import pl.dolien.shop.email.activationAccount.AccountActivationEmailService;
import pl.dolien.shop.email.activationAccount.AccountActivationMessageDTO;
import pl.dolien.shop.exception.UserAlreadyExistsException;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RoleService roleService;
    private final UserService userService;
    private final AccountActivationEmailService emailService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void registerUser(RegistrationDTO request) throws MessagingException, RoleNotFoundException {
        validateUserNotExists(request.getEmail());

        User user = createUser(request);
        userService.saveUser(user);
        sendActivationEmail(user);
    }

    private void validateUserNotExists(String email) {
        if (userService.isUserExists(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
    }

    private User createUser(RegistrationDTO dto) throws RoleNotFoundException {
        Role userRole = roleService.getByName("USER");

        return User.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .dateOfBirth(dto.getDateOfBirth())
                .password(passwordEncoder.encode(dto.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
    }

    private void sendActivationEmail (User user) throws MessagingException {
        String activationToken = tokenService.generateActivationToken(user);

        emailService.sendActivationEmail(
                AccountActivationMessageDTO.builder()
                        .to(user.getEmail())
                        .username(user.getFullName())
                        .subject("Account activation")
                        .emailTemplate(EmailTemplateName.ACTIVATE_ACCOUNT)
                        .confirmationUrl(activationUrl + activationToken)
                        .activationCode(activationToken)
                        .build()
        );
    }
}