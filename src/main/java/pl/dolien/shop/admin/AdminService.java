package pl.dolien.shop.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dolien.shop.role.RoleRepository;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserEditDTO;
import pl.dolien.shop.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addAdmin(String email) {
        var user = userRepository.findByEmail(email).orElse(null);
        assert user != null;
        roleRepository.findByName("ADMIN").ifPresent(user::addRole);
        userRepository.save(user);
    }

    public void editUser(UserEditDTO user) {
        var userFromDB = userRepository.findById(user.getId()).orElse(null);
        assert userFromDB != null;
        if(userRepository.findByEmail(user.getEmail()).isPresent() && !user.getEmail().equals(userFromDB.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        userFromDB.setFirstname(user.getFirstname());
        userFromDB.setLastname(user.getLastname());
        userFromDB.setEmail(user.getEmail());
        if(user.getPassword() != null) {
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userFromDB.setAccountLocked(user.isAccountLocked());
        userFromDB.setDateOfBirth(user.getDateOfBirth());
        userRepository.save(userFromDB);
    }
}
