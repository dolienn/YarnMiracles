package pl.dolien.shop.product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.settings.AppSettings;
import pl.dolien.shop.settings.AppSettingsRepository;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;
    private final AppSettingsRepository appSettingsRepository;

    public void updateProductImages() {
        AppSettings settings = appSettingsRepository.findById(1L).orElse(new AppSettings());

        if (settings.isImagesUpdated()) {
            return;
        }


        List<Product> products = productRepository.findAll();
        String localImagePath = "shop-ui/src/assets/images/products/";

        for (Product product : products) {
            String localImageFileName = product.getImageUrl().substring(product.getImageUrl().lastIndexOf("/") + 1);
            File localFile = new File(localImagePath + localImageFileName);

            if (localFile.exists()) {
                try {
                    Map imageInfo = cloudinary.uploader().upload(localFile, ObjectUtils.asMap("resource_type", "image", "image_metadata", true));
                    int width = (int) imageInfo.get("width");
                    int height = (int) imageInfo.get("height");

                    String transformation;
                    if (height > width) {
                        transformation = "w_171,h_171,c_fill";
                    } else {
                        transformation = "w_171,h_171,c_pad,b_auto";
                    }

                    Map uploadResult = cloudinary.uploader().upload(localFile, ObjectUtils.asMap("transformation", transformation));
                    String imageUrl = (String) uploadResult.get("secure_url");

                    product.setImageUrl(imageUrl);
                    productRepository.save(product);

                    settings.setImagesUpdated(true);
                    appSettingsRepository.save(settings);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File does not exist: " + localFile.getAbsolutePath());
            }
        }
    }

    public String generateSku(String productName, Long productId) {
        String namePart = productName.substring(0, Math.min(3, productName.length())).toUpperCase();
        String idPart = String.format("%04d", productId);
        return namePart + "-" + idPart;
    }
}
