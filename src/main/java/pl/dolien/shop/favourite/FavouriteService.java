package pl.dolien.shop.favourite;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;

public interface FavouriteService {

    Page<ProductDTO> getFavourites(Integer userId, PaginationAndSortParams paginationAndSortParams, Authentication connectedUser);

    void addToFavourites(Integer userId, Long productId, Authentication connectedUser);

    void removeFromFavourites(Integer userId, Long productId, Authentication connectedUser);
}
