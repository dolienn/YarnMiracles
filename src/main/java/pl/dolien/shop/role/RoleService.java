package pl.dolien.shop.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getByName(String name) throws RoleNotFoundException {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role " + name + " not found"));
    }
}
