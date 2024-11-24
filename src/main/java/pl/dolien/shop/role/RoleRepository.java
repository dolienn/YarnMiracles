package pl.dolien.shop.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String role);

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id IN :userIds")
    List<Role> findAllByUserIds(List<Integer> userIds);
}
