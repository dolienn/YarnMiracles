package pl.dolien.shop.image;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import pl.dolien.shop.exception.ImageReadException;
import pl.dolien.shop.exception.ImageUploadException;
import pl.dolien.shop.file.FileValidator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

class ImageUploaderTest {

    private static final String UNABLE_TO_READ_IMAGE_MESSAGE = "Unable to read image from file";
    private static final String UNABLE_TO_UPLOAD_IMAGE_MESSAGE = "Error occurred while uploading image: null";
    private static final String MISSING_SECURE_URL_MESSAGE = "Secure URL is missing in the upload result";
    private static final String CLOUDINARY_SECURE_URL = "https://cloudinary.com/secure_url";
    private static final String FILE_NAME = "file";

    @InjectMocks
    private ImageUploader imageUploader;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private com.cloudinary.Uploader uploader;

    private final Map<String, Object> uploadResponse = new HashMap<>();
    private MockMultipartFile image;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadImageSuccessfully() throws IOException {
        image = createMockImageFile();

        mockCloudinaryUploadSuccess();

        String result = imageUploader.uploadImage(image);

        assertEquals(CLOUDINARY_SECURE_URL, result);
        verify(fileValidator, times(1)).validateFileIsNotEmpty(image);
    }

    @Test
    void shouldThrowExceptionWhenImageIsEmpty() {
        image = createEmptyImageFile();

        ImageReadException exception = assertThrows(
                ImageReadException.class, () -> imageUploader.uploadImage(image)
        );

        assertEquals(UNABLE_TO_READ_IMAGE_MESSAGE, exception.getMessage());
        verify(fileValidator, times(1)).validateFileIsNotEmpty(image);
    }

    @Test
    void shouldThrowExceptionWhenUploadFails() throws IOException {
        image = createMockImageFile();

        mockCloudinaryUploadFailure();

        ImageUploadException exception = assertThrows(
                ImageUploadException.class, () -> imageUploader.uploadImage(image)
        );

        assertEquals(UNABLE_TO_UPLOAD_IMAGE_MESSAGE, exception.getMessage());
        verify(fileValidator, times(1)).validateFileIsNotEmpty(image);
    }

    @Test
    void shouldThrowExceptionWhenSecureUrlIsNull() throws IOException {
        image = createMockImageFile();

        mockCloudinaryUploadNoSecureUrl();

        ImageUploadException exception = assertThrows(
                ImageUploadException.class, () -> imageUploader.uploadImage(image)
        );

        assertEquals(MISSING_SECURE_URL_MESSAGE, exception.getMessage());
        verify(fileValidator, times(1)).validateFileIsNotEmpty(image);
    }

    private void mockCloudinaryUploadSuccess() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResponse);
        addSecureUrl();
    }

    private void addSecureUrl() {
        uploadResponse.put("secure_url", CLOUDINARY_SECURE_URL);
    }

    private void mockCloudinaryUploadFailure() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenThrow(new IOException());
    }

    private void mockCloudinaryUploadNoSecureUrl() throws IOException {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResponse);
    }

    private static MockMultipartFile createMockImageFile() throws IOException {
        BufferedImage img = new BufferedImage(100, 100, TYPE_INT_RGB);
        img.getGraphics().fillRect(0, 0, 100, 100);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);

        byte[] imageBytes = baos.toByteArray();

        return new MockMultipartFile(FILE_NAME, "image.jpg", IMAGE_JPEG_VALUE, imageBytes);
    }

    private static MockMultipartFile createEmptyImageFile() {
        byte[] emptyContent = new byte[0];
        return new MockMultipartFile(FILE_NAME, "empty-image.jpg", IMAGE_JPEG_VALUE, emptyContent);
    }
}
