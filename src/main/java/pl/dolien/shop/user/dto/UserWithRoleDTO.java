package pl.dolien.shop.user.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.dolien.shop.role.dto.RoleDTO;

import java.util.Set;

@Getter
@SuperBuilder
public class UserWithRoleDTO extends UserDTO {
    private Set<RoleDTO> roles;
}
