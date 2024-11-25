package pl.dolien.shop.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) throws RoleNotFoundException {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role " + name + " not found"));
    }
}
