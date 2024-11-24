package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.dto.RoleDTO;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.dolien.shop.role.RoleMapper.toRoleDTOs;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUser(User user,
                       UserRequestDTO requestDTO) {
        user.setFirstname(requestDTO.getFirstname());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setAccountLocked(requestDTO.isAccountLocked());
        user.setDateOfBirth(requestDTO.getDateOfBirth());

        if (requestDTO.getPassword() != null)
            user.setPassword(encodePassword(requestDTO.getPassword()));

        return user;
    }

    public static User toUser(UserWithRoleDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .email(userDTO.getEmail())
                .dateOfBirth(userDTO.getDateOfBirth())
                .accountLocked(userDTO.isAccountLocked())
                .enabled(userDTO.isEnabled())
                .createdDate(userDTO.getCreatedDate())
                .lastModifiedDate(userDTO.getLastModifiedDate())
                .build();
    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .accountLocked(user.isAccountLocked())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }

    public static Page<UserWithRoleDTO> toUserWithRoleDTOs(Page<User> users, List<Role> roles) {
        List<UserWithRoleDTO> userWithRoleDTOs = users.stream()
                .map(user -> toUserWithRoleDTO(user, roles))
                .collect(Collectors.toList());

        Pageable pageable = users.getPageable();

        return new RestPage<>(userWithRoleDTOs, pageable.getPageNumber(), pageable.getPageSize(), users.getTotalElements());
    }

    public static UserWithRoleDTO toUserWithRoleDTO(User user, List<Role> roles) {
        Set<RoleDTO> userRoles = extractRoles(roles, user.getId());

        return UserWithRoleDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .accountLocked(user.isAccountLocked())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .roles(userRoles)
                .build();
    }

    private static Set<RoleDTO> extractRoles(List<Role> roles, Integer userId) {
        Set<Role> userRoles = roles.stream()
                .filter(role -> role.getUsers().stream()
                        .anyMatch(user -> user.getId().equals(userId)))
                .collect(Collectors.toSet());

        return toRoleDTOs(userRoles);
    }

    public static UserWithRoleDTO toUserWithRoleDTO(User user) {
        return UserWithRoleDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .accountLocked(user.isAccountLocked())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .roles(toRoleDTOs(user.getRoles()))
                .build();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
