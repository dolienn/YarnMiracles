package pl.dolien.shop.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ImageReadException;
import pl.dolien.shop.exception.ImageUploadException;
import pl.dolien.shop.file.FileService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploader {

    private final Cloudinary cloudinary;
    private final ImageTransformer imageTransformer;
    private final FileService fileService;

    public String uploadImage(MultipartFile file) {
        fileService.validateFileIsNotEmpty(file);

        BufferedImage img = readImage(file);
        String transformation = imageTransformer.getTransformation(img);

        Map<String, Object> uploadResult = uploadToCloudinary(file, transformation);

        return getSecureUrl(uploadResult);
    }

    private BufferedImage readImage(MultipartFile file) {
        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            validateImage(img);
            return img;
        } catch (IOException e) {
            throw new ImageReadException("Error occurred while reading the image: " + e.getMessage());
        }
    }

    private void validateImage(BufferedImage img) {
        if (img == null) {
            throw new ImageReadException("Unable to read image from file");
        }
    }

    private Map<String, Object> uploadToCloudinary(MultipartFile file, String transformation) {
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", UUID.randomUUID().toString(),
                            "resource_type", "image",
                            "transformation", transformation
                    ));
        } catch (IOException e) {
            throw new ImageUploadException("Error occurred while uploading image: " + e.getMessage());
        }
    }

    private String getSecureUrl(Map<String, Object> uploadResult) {
        return uploadResult.get("secure_url").toString();
    }
}