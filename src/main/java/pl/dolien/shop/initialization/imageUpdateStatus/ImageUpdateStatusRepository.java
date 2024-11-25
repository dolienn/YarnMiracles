package pl.dolien.shop.initialization.imageUpdateStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ImageUpdateStatusRepository extends JpaRepository<ImageUpdateStatus, Long> {
}
