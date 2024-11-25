package pl.dolien.shop.auth.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.email.EmailTemplateName;
import pl.dolien.shop.email.activationAccount.AccountActivationMessageDTO;
import pl.dolien.shop.exception.UserAlreadyExistsException;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RoleService roleService;
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final KafkaJsonProducer kafkaJsonProducer;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Override
    public User registerUser(RegistrationDTO dto) throws RoleNotFoundException {
        validateUserNotExists(dto.getEmail());

        User user = createUser(dto);
        User savedUser = userService.saveUser(user);

        AccountActivationMessageDTO message = createActivationMessage(savedUser);
        kafkaJsonProducer.sendMessageToSendActivationEmail(message);

        return savedUser;
    }

    private void validateUserNotExists(String email) {
        if (userService.isUserExists(email))
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
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
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
    }

    private AccountActivationMessageDTO createActivationMessage(User savedUser) {
        String activationToken = tokenService.generateActivationToken(savedUser);
        return AccountActivationMessageDTO.builder()
                .to(savedUser.getEmail())
                .username(savedUser.getFullName())
                .subject("Account activation")
                .emailTemplate(EmailTemplateName.ACTIVATE_ACCOUNT)
                .confirmationUrl(activationUrl + activationToken)
                .activationCode(activationToken)
                .build();
    }
}