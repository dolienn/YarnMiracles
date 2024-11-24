package pl.dolien.shop.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserWithRoleDTO> getAllUsers(@ModelAttribute PaginationAndSortParams paginationAndSortParams,
                                             Authentication connectedUser) {
        return userService.getAllUsers(paginationAndSortParams, connectedUser);
    }

    @GetMapping("/{userId}")
    public UserWithRoleDTO getUserWithRolesById(@PathVariable Integer userId, Authentication connectedUser) {
        return userService.getUserWithRolesById(userId, connectedUser);
    }

    @GetMapping("/byAuth")
    public UserWithRoleDTO getUserByAuth(Authentication connectedUser) {
        return userService.getUserByAuth(connectedUser);
    }

    @PutMapping
    public UserDTO editUser(@RequestBody @Valid UserRequestDTO user, Authentication connectedUser) {
        return userService.editUser(user, connectedUser);
    }

    @GetMapping("/{email}/roles/{roleName}")
    public UserWithRoleDTO addRole(@PathVariable String email,
                                   @PathVariable String roleName,
                                   Authentication connectedUser) throws RoleNotFoundException {
        return userService.addRole(email, roleName, connectedUser);
    }

    @DeleteMapping("/{email}/roles/{roleName}")
    public void removeRole(@PathVariable String email,
                           @PathVariable String roleName,
                           Authentication connectedUser) throws RoleNotFoundException {
        userService.removeRole(email, roleName, connectedUser);
    }

    @GetMapping("/{userId}/purchased/{productId}")
    public Boolean hasUserPurchasedProduct(
            @PathVariable Integer userId,
            @PathVariable Long productId
    ) {
        return userService.hasUserPurchasedProduct(userId, productId);
    }

    @GetMapping("/{userId}/quantityOfPurchasedProducts")
    public Integer getNumberOfPurchasedProducts(@PathVariable Integer userId) {
        return userService.getNumberOfPurchasedProducts(userId);
    }
}
