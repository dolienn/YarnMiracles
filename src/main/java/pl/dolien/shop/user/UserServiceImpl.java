package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.exception.UserNotFoundException;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleRepository;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;

import java.util.List;

import static pl.dolien.shop.user.UserMapper.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageableBuilder pageableBuilder;

    @Cacheable(cacheNames = "users", keyGenerator = "customKeyGenerator")
    @Override
    public Page<UserWithRoleDTO> getAllUsers(PaginationAndSortParams paginationAndSortParams, Authentication connectedUser) {
        verifyUserHasAdminRole(connectedUser);

        Pageable pageable = pageableBuilder.buildPageable(paginationAndSortParams);
        Page<User> users = userRepository.findAll(pageable);
        List<Integer> userIds = users.stream()
                .map(User::getId)
                .toList();
        List<Role> roles = roleRepository.findAllByUserIds(userIds);

        return toUserWithRoleDTOs(users, roles);
    }

    @Cacheable(cacheNames = "user", keyGenerator = "customKeyGenerator")
    @Override
    public UserWithRoleDTO getUserWithRolesById(Integer userId, Authentication connectedUser) {
        verifyUserHasAdminRole(connectedUser);
        return toUserWithRoleDTO(getUserById(userId));
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Cacheable(cacheNames = "connectedUser", keyGenerator = "customKeyGenerator")
    @Override
    public UserWithRoleDTO getUserByAuth(Authentication connectedUser) {

        if (connectedUser == null)
            throw new UserNotFoundException("Authenticated user not found");

        User user = (User) connectedUser.getPrincipal();
        return toUserWithRoleDTO(user);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(cacheNames = {"connectedUser", "users", "user"}, allEntries = true)
    @Override
    public UserDTO editUser(UserRequestDTO userDTO, Authentication connectedUser) {
        verifyUserHasAdminRole(connectedUser);
        User userFromDB = getUserById(userDTO.getId());

        assertEmailNotInUse(userDTO.getEmail(), userFromDB.getEmail());

        User updatedUser = userMapper.toUser(userFromDB, userDTO);
        return toUserDTO(saveUser(updatedUser));
    }

    @CacheEvict(cacheNames = {"connectedUser", "users", "user"}, allEntries = true)
    @Override
    public UserWithRoleDTO addRole(String email, String roleName, Authentication connectedUser) throws RoleNotFoundException {
        verifyUserHasAdminRole(connectedUser);
        User userFromDB = getUserByEmail(email);

        Role role = roleService.getByName(roleName);
        userFromDB.addToRoles(role);

        return toUserWithRoleDTO(saveUser(userFromDB));
    }

    @CacheEvict(cacheNames = {"connectedUser", "users", "user"}, allEntries = true)
    @Override
    public void removeRole(String email, String roleName, Authentication connectedUser) throws RoleNotFoundException {
        verifyUserHasAdminRole(connectedUser);
        User userFromDB = getUserByEmail(email);

        Role role = roleService.getByName(roleName);
        userFromDB.removeFromRoles(role);
        saveUser(userFromDB);
    }

    @Override
    public boolean hasUserPurchasedProduct(Integer userId, Long productId) {
        User user = getUserById(userId);
        if (user == null || user.getPurchasedProducts() == null) {
            return false;
        }

        return user.getPurchasedProducts().stream()
                .anyMatch(product -> product.getId().equals(productId));
    }

    @Override
    public Integer getNumberOfPurchasedProducts(Integer userId) {
        User user = getUserById(userId);
        if (user == null || user.getPurchasedProducts() == null) {
            return 0;
        }

        return user.getPurchasedProducts().size();
    }

    @Override
    public void verifyUserHasAdminRole(Authentication connectedUser) {
        UserWithRoleDTO userDTO = getUserByAuth(connectedUser);
        User userFromDB = getUserById(userDTO.getId());

        if (userFromDB.getRoles().stream()
                .noneMatch(
                        role -> role.getName().equals("ADMIN")
                )
        ) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
    }

    @Override
    public void verifyUserIsAuthenticatedUser(Integer userId, Authentication connectedUser) {
        if (!userId.equals(getUserByAuth(connectedUser).getId()))
            throw new AccessDeniedException("Authenticated user does not match the requested user");
    }

    @Override
    public void assertEmailNotInUse(String newEmail, String currentEmail) {
        if(isEmailTaken(newEmail, currentEmail))
            throw new EmailAlreadyExistsException("User with email " + newEmail + " already exists");
    }

    @Override
    public boolean isEmailTaken(String email, String userFromDBEmail) {
        return isUserExists(email) && !email.equals(userFromDBEmail);
    }

    @Override
    public boolean isUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
