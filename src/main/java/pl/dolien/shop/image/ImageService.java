package pl.dolien.shop.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String transformation;

        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            int width = img.getWidth();
            int height = img.getHeight();

            if (height > width) {
                transformation = "w_171,h_171,c_fill";
            } else {
                transformation = "w_171,h_171,c_pad,b_auto";
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", UUID.randomUUID().toString(),
                            "resource_type", "image",
                            "transformation", transformation
                    ));

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new IOException("Error occurred while uploading image: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while uploading the image: " + e.getMessage(), e);
        }
    }

}