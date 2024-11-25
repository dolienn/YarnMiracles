package pl.dolien.shop.role;

import pl.dolien.shop.role.dto.RoleDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static Set<RoleDTO> toRoleDTOs(Set<Role> roles) {
        return roles.stream()
                .map(RoleMapper::toRoleDTO)
                .collect(Collectors.toSet());
    }

    public static RoleDTO toRoleDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .createdDate(role.getCreatedDate())
                .lastModifiedDate(role.getLastModifiedDate())
                .build();
    }
}
