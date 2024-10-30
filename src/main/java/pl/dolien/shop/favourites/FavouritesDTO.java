package pl.dolien.shop.favourites;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavouritesDTO {
    private Integer userId;
    private String sortOrderType;
    private Integer page = 0;
    private Integer size = 20;
}
