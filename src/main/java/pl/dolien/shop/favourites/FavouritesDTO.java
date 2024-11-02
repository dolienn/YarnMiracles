package pl.dolien.shop.favourites;

import lombok.Getter;
import lombok.Setter;
import pl.dolien.shop.pagination.PageRequestParams;

@Getter
@Setter
public class FavouritesDTO {
    private Integer userId;
    private PageRequestParams pageRequestParams;
}
