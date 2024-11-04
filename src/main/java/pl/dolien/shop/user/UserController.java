package pl.dolien.shop.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/byAuth")
    public User getUserByAuth(Authentication connectedUser) {
        return userService.getUserByAuth(connectedUser);
    }

    @PutMapping
    public ResponseEntity<Void> editUser(@RequestBody @Valid UserDTO user) {
        userService.editUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}/roles/{roleName}")
    public ResponseEntity<Void> addRole(@PathVariable String email, @PathVariable String roleName) throws RoleNotFoundException {
        userService.addRole(email, roleName);
        return ResponseEntity.ok().build();
    }
}
