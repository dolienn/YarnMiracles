package pl.dolien.shop.exception;

public class InvalidSortOrderException extends RuntimeException {
    public InvalidSortOrderException(String message) {
        super(message);
    }
}
