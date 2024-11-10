package pl.dolien.shop.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;

import static pl.dolien.shop.user.UserMapper.toUserDTO;
import static pl.dolien.shop.user.UserMapper.toUserWithRoleDTO;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping("/byAuth")
    public UserDTO getUserByAuth(Authentication connectedUser) {
        return userService.getUserDTOByAuth(connectedUser);
    }

    @PutMapping
    public UserDTO editUser(@RequestBody @Valid UserRequestDTO user, Authentication connectedUser) {
        return userService.editUser(user, connectedUser);
    }

    @GetMapping("/{email}/roles/{roleName}")
    public UserWithRoleDTO addRole(@PathVariable String email, @PathVariable String roleName, Authentication connectedUser) throws RoleNotFoundException {
        return userService.addRole(email, roleName, connectedUser);
    }
}
