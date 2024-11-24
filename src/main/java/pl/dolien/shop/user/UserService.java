package pl.dolien.shop.user;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;

public interface UserService {

    Page<UserWithRoleDTO> getAllUsers(PaginationAndSortParams paginationAndSortParams, Authentication connectedUser);

    UserWithRoleDTO getUserWithRolesById(Integer userId, Authentication connectedUser);

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    UserWithRoleDTO getUserByAuth(Authentication connectedUser);

    User saveUser(User user);

    UserDTO editUser(UserRequestDTO userDTO, Authentication connectedUser);

    UserWithRoleDTO addRole(String email, String roleName, Authentication connectedUser) throws RoleNotFoundException;

    void removeRole(String email, String roleName, Authentication connectedUser) throws RoleNotFoundException;

    boolean hasUserPurchasedProduct(Integer userId, Long productId);

    Integer getNumberOfPurchasedProducts(Integer userId);

    void verifyUserHasAdminRole(Authentication connectedUser);

    void verifyUserIsAuthenticatedUser(Integer userId, Authentication connectedUser);

    void assertEmailNotInUse(String newEmail, String currentEmail);

    boolean isEmailTaken(String email, String userFromDBEmail);

    boolean isUserExists(String email);
}
