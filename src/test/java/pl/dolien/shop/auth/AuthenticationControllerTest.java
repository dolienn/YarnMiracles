//package pl.dolien.shop.auth;
//
//import jakarta.mail.MessagingException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.Authentication;
//import pl.dolien.shop.auth.activation.ActivationController;
//import pl.dolien.shop.auth.activation.ActivationService;
//import pl.dolien.shop.auth.login.LoginRequest;
//import pl.dolien.shop.auth.login.LoginController;
//import pl.dolien.shop.auth.login.LoginService;
//import pl.dolien.shop.auth.password.ChangePasswordDTO;
//import pl.dolien.shop.auth.password.PasswordChanger;
//import pl.dolien.shop.auth.password.PasswordController;
//import pl.dolien.shop.auth.registration.RegistrationDTO;
//import pl.dolien.shop.auth.registration.RegistrationController;
//import pl.dolien.shop.auth.registration.RegistrationService;
//import pl.dolien.shop.auth.userInfo.UserInfoController;
//import pl.dolien.shop.auth.userInfo.UserInfoUpdater;
//import pl.dolien.shop.user.User;
//import pl.dolien.shop.user.UserController;
//
//import javax.management.relation.RoleNotFoundException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//public class AuthenticationControllerTest {
//
//    @InjectMocks
//    private PasswordController passwordController;
//
//    @InjectMocks
//    private ActivationController activationController;
//
//    @InjectMocks
//    private LoginController loginController;
//
//    @InjectMocks
//    private RegistrationController registrationController;
//
//    @InjectMocks
//    private UserInfoController userInfoController;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private RegistrationService registrationService;
//
//    @Mock
//    private PasswordChanger passwordChanger;
//
//    @Mock
//    private UserInfoUpdater userInfoUpdater;
//
//    @Mock
//    private LoginService loginService;
//
//    @Mock
//    private ActivationService activationService;
//
//    @Mock
//    private Authentication auth;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void shouldRegister() throws MessagingException, RoleNotFoundException {
//        registrationController.register(
//                RegistrationDTO.builder()
//                        .firstname("John")
//                        .lastname("Doe")
//                        .email("john.doe@example.com")
//                        .password("password123")
//                        .build()
//        );
//
//        verify(registrationService, times(1)).registerUser(any(RegistrationDTO.class));
//    }
//
//    @Test
//    public void shouldChangeAccountDetails() {
//        userInfoController.updateUserInfo(
//                RegistrationDTO.builder()
//                        .firstname("John")
//                        .lastname("Doe")
//                        .email("john.doe@example.com")
//                        .password("password123")
//                        .build(),
//                auth
//        );
//
//        verify(userInfoUpdater, times(1)).updateUserInformation(any(RegistrationDTO.class), any(Authentication.class));
//    }
//
//    @Test
//    public void shouldChangePassword() {
//        passwordController.changePassword(
//                ChangePasswordDTO.builder()
//                        .yourPassword("password123")
//                        .newPassword("newPassword123")
//                        .build(),
//                auth
//        );
//
//        verify(passwordChanger, times(1)).changePassword(any(ChangePasswordDTO.class), any(Authentication.class));
//    }
//
//    @Test
//    public void shouldAuthenticate() {
//        loginController.login(
//                LoginRequest.builder()
//                        .email("john.doe@example.com")
//                        .password("password123")
//                        .build()
//        );
//
//        verify(loginService, times(1)).login(any(LoginRequest.class));
//    }
//
//    @Test
//    public void shouldActivateAccount() {
//        activationController.activateUser(
//                "token");
//
//        verify(activationService, times(1)).activateUser(any(String.class));
//    }
//
//    @Test
//    public void shouldGetUserInfo() {
//        User response = userController.getUserByAuth(auth);
//
//        verify(auth, times(1)).getPrincipal();
//    }
//}
