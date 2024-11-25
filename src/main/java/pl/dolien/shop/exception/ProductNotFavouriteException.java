package pl.dolien.shop.exception;

public class ProductNotFavouriteException extends RuntimeException {
    public ProductNotFavouriteException(String message) {
        super(message);
    }
}
