package pl.dolien.shop.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.role.RoleRepository;
import pl.dolien.shop.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public void addAdmin(String email) {
        var user = userRepository.findByEmail(email).orElse(null);
        assert user != null;
        roleRepository.findByName("ADMIN").ifPresent(user::addRole);
        userRepository.save(user);
    }


}
