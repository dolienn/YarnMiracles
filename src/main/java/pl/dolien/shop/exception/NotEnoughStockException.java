package pl.dolien.shop.exception;

public class NotEnoughStockException extends RuntimeException {
  public NotEnoughStockException(String message) {
    super(message);
  }
}
