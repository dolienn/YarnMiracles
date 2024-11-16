package pl.dolien.shop.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ImageReadException;
import pl.dolien.shop.exception.ImageUploadException;
import pl.dolien.shop.file.FileValidator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import static pl.dolien.shop.image.ImageTransformer.getTransformation;

@Service
@RequiredArgsConstructor
public class ImageUploader {

    private final Cloudinary cloudinary;
    private final FileValidator fileValidator;

    public String uploadImage(MultipartFile file) {
        fileValidator.validateFileIsNotEmpty(file);

        BufferedImage img = readAndValidateImage(file);
        String transformation = getTransformation(img);

        Map<String, Object> uploadResult = uploadImageWithTransformation(file, transformation);

        return getSecureUrl(uploadResult);
    }

    protected BufferedImage readImage(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    protected BufferedImage readAndValidateImage(MultipartFile file) {
        try {
            BufferedImage img = readImage(file.getInputStream());
            validateImageIsNotNull(img);
            return img;
        } catch (IOException e) {
            throw new ImageReadException("Error occurred while reading the image: " + e.getMessage());
        }
    }

    private void validateImageIsNotNull(BufferedImage img) {
        if (img == null)
            throw new ImageReadException("Unable to read image from file");
    }

    private Map<String, Object> uploadImageWithTransformation(MultipartFile file, String transformation) {
        try {
            return uploadImageToCloudinary(file, transformation);
        } catch (IOException e) {
            throw new ImageUploadException("Error occurred while uploading image: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> uploadImageToCloudinary(MultipartFile file, String transformation) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", UUID.randomUUID().toString(),
                        "resource_type", "image",
                        "transformation", transformation
                ));
    }

    private String getSecureUrl(Map<String, Object> uploadResult) {
        Object secureUrl = uploadResult.get("secure_url");

        if (secureUrl == null) {
            throw new ImageUploadException("Secure URL is missing in the upload result");
        }

        return secureUrl.toString();
    }
}