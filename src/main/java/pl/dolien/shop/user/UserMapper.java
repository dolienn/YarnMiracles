package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import static pl.dolien.shop.role.RoleMapper.toRoleDTOs;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public static User toUser(User user,
                                  UserRequestDTO requestDTO,
                                  PasswordEncoder passwordEncoder) {
        user.setFirstname(requestDTO.getFirstname());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setAccountLocked(requestDTO.isAccountLocked());
        user.setDateOfBirth(requestDTO.getDateOfBirth());

        if (requestDTO.getPassword() != null) {
            user.setPassword(encodePassword(requestDTO.getPassword(), passwordEncoder));
        }

        return user;
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

    private static String encodePassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
