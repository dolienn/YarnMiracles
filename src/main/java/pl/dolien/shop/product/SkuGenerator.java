package pl.dolien.shop.product;

public class SkuGenerator {

    public static String generateSku(String productName, Long productId) {
        String namePart = productName.substring(0, Math.min(3, productName.length())).toUpperCase();
        String idPart = String.format("%04d", productId);
        return namePart + "-" + idPart;
    }
}
