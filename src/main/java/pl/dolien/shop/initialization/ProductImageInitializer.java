package pl.dolien.shop.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ImageNotFoundException;
import pl.dolien.shop.image.ImageUploader;
import pl.dolien.shop.initialization.imageUpdateStatus.ImageUpdateStatus;
import pl.dolien.shop.initialization.imageUpdateStatus.ImageUpdateStatusService;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductImageInitializer {

    private static final String LOCAL_IMAGE_PATH = "shop-ui/src/assets/images/products/";

    private final ProductService productService;
    private final ImageUpdateStatusService imageUpdateStatusService;
    private final ImageUploader imageUploader;

    @Transactional
    public void updateAllProductImages() {
        ImageUpdateStatus imageUpdateStatus = imageUpdateStatusService.getImageUpdateStatus();

        if (imageUpdateStatus.isImagesUpdated())
            return;

        List<Product> products = productService.getAllProducts();
        products.forEach(this::processProductImage);

        imageUpdateStatusService.updateImageStatus(imageUpdateStatus, true);
    }

    private void processProductImage(Product product) {
        String localImageFileName = extractLocalImageFileName(product.getImageUrl());
        File localFile = new File(LOCAL_IMAGE_PATH + localImageFileName);

        if (localFile.exists()) {
            uploadAndSetProductImage(product, localFile);
        } else {
            throw new ImageNotFoundException("Image not found: " + localImageFileName);
        }
    }

    private String extractLocalImageFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    private void uploadAndSetProductImage(Product product, File localFile) {
        MultipartFile multipartFile = new FileMultipartFile(localFile);
        String imageUrl = imageUploader.uploadImage(multipartFile);

        product.setImageUrl(imageUrl);
        productService.saveProduct(product);
    }
}