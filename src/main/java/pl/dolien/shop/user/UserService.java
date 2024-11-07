package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.exception.UserNotFoundException;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.user.dto.UserRequestDTO;

import javax.management.relation.RoleNotFoundException;

import static pl.dolien.shop.user.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User addRole(String email, String roleName, Authentication connectedUser) throws RoleNotFoundException {
        verifyUserHasAdminRole(connectedUser);
        User user = getUserByEmail(email);

        Role role = roleService.getByName(roleName);
        user.addToRoles(role);

        return saveUser(user);
    }

    public User getUserByAuth(Authentication connectedUser) {
        if (connectedUser == null) {
            throw new UserNotFoundException("User not found");
        }
        return (User) connectedUser.getPrincipal();
    }

    public void verifyUserIsAuthenticatedUser(Integer userId, Authentication connectedUser) {
        User user = getUserById(userId);
        if (!user.equals(getUserByAuth(connectedUser))) {
            throw new AccessDeniedException("Authenticated user does not match the requested user");
        }
    }

    public User editUser(UserRequestDTO userDto, Authentication connectedUser) {
        verifyUserHasAdminRole(connectedUser);
        User userFromDB = getUserById(userDto.getId());

        if (isEmailTaken(userDto.getEmail(), userFromDB.getEmail())) {
            throw new EmailAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }

        User updatedUser = toUser(userFromDB, userDto, passwordEncoder);
        return saveUser(updatedUser);
    }

    public void verifyUserHasAdminRole(Authentication connectedUser) {
        User user = getUserByAuth(connectedUser);
        if (user.getRoles().stream()
                .noneMatch(
                        role -> role.getName().equals("ADMIN")
                )
        ) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
    }

    public boolean isEmailTaken(String email, String userFromDBEmail) {
        return isUserExists(email) && !email.equals(userFromDBEmail);
    }

    public boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
