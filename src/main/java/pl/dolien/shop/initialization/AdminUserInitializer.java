package pl.dolien.shop.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_FIRST_NAME = "Admin";
    private static final String ADMIN_LAST_NAME = "Admin";
    private static final String ADMIN_PASSWORD = "testadmin123";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public void initializeAdminUser() throws RoleNotFoundException {
        if (isAdminUserNotExists()) {
            User adminUser = createAdminUser();
            userService.saveUser(adminUser);
        }
    }

    private boolean isAdminUserNotExists() {
        return !userService.isUserExists(ADMIN_EMAIL);
    }

    private User createAdminUser() throws RoleNotFoundException {
        return User.builder()
                .firstname(ADMIN_FIRST_NAME)
                .lastname(ADMIN_LAST_NAME)
                .email(ADMIN_EMAIL)
                .dateOfBirth(LocalDate.now())
                .password(encodeAdminPassword())
                .accountLocked(false)
                .enabled(true)
                .roles(List.of(getAdminRole()))
                .build();
    }

    private Role getAdminRole() throws RoleNotFoundException {
        return roleService.getByName("ADMIN");
    }

    private String encodeAdminPassword() {
        return passwordEncoder.encode(AdminUserInitializer.ADMIN_PASSWORD);
    }
}
