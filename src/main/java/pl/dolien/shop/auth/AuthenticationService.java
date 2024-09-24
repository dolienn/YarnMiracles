package pl.dolien.shop.auth;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.dashboard.DashboardDataRepository;
import pl.dolien.shop.email.EmailService;
import pl.dolien.shop.email.EmailTemplateName;
import pl.dolien.shop.role.RoleRepository;
import pl.dolien.shop.security.JwtService;
import pl.dolien.shop.user.Token;
import pl.dolien.shop.user.TokenRepository;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final DashboardDataRepository dashboardDataRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userFromDB = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (userFromDB != null) {
            throw new IllegalStateException("User with email " + request.getEmail() + " already exists");
        }

        var userRole =  roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void changeAccountDetails(RegistrationRequest request, Authentication auth) {
        assert auth != null;

        User currentUser = (User) auth.getPrincipal();
        String encodedCurrentPassword = currentUser.getPassword();

        if (!passwordEncoder.matches(request.getPassword(), encodedCurrentPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || Objects.equals(request.getEmail(), currentUser.getEmail())) {
            User authUser = userRepository.findByEmail(currentUser.getEmail()).orElse(null);
            if (authUser != null) {
                authUser.setFirstname(request.getFirstname());
                authUser.setLastname(request.getLastname());
                authUser.setEmail(request.getEmail());
                authUser.setDateOfBirth(request.getDateOfBirth());
                userRepository.save(authUser);
            }
        } else {
            throw new IllegalArgumentException("There is a user on the same email.");
        }
    }

    public void changePassword(PasswordRequest request, Authentication auth) {
        assert auth != null;

        User currentUser = (User) auth.getPrincipal();
        String encodedCurrentPassword = currentUser.getPassword();

        if (!passwordEncoder.matches(request.getYourPassword(), encodedCurrentPassword)) {
            throw new IllegalArgumentException("The current password does not match the one entered in the form.");
        }

        if (Objects.equals(request.getYourPassword(), request.getNewPassword())) {
            throw new IllegalArgumentException("The current password and the new one are the same.");
        }

        User user = userRepository.findByEmail(currentUser.getEmail()).orElse(null);

        if(user != null) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        int length = 6;
        String generatedToken = generateActivationCode(length);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. " +
                    "A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

        dashboardDataRepository.findById(1L).ifPresent(dashboardData -> {
            dashboardData.setTotalUsers(dashboardData.getTotalUsers() + 1);
            dashboardDataRepository.save(dashboardData);
        });
    }
}
