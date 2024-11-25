package pl.dolien.shop.exception;

public class ProductAlreadyFavouriteException extends RuntimeException {
    public ProductAlreadyFavouriteException(String message) {
        super(message);
    }
}
