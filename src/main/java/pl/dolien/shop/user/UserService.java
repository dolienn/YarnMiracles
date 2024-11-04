package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.exception.UserNotFoundException;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void addRole(String email, String roleName) throws RoleNotFoundException {
        User user = getUserByEmail(email);

        Role role = roleService.getByName(roleName);
        user.addToRoles(role);

        saveUser(user);
    }

    public User getUserByAuth(Authentication connectedUser) {
        if (connectedUser == null) {
            throw new UserNotFoundException("User not found");
        }
        return (User) connectedUser.getPrincipal();
    }

    public void editUser(UserDTO userDto) {
        User userFromDB = getUserById(userDto.getId());

        if (isEmailTaken(userDto.getEmail(), userFromDB)) {
            throw new EmailAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }

        User updatedUser = userMapper.updateUserFromDto(userFromDB, userDto);
        saveUser(updatedUser);
    }

    private boolean isEmailTaken(String email, User userFromDB) {
        return userRepository.findByEmail(email).isPresent() && !email.equals(userFromDB.getEmail());
    }
}
