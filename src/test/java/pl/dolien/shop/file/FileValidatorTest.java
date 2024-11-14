package pl.dolien.shop.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.DirectoryCreationException;
import pl.dolien.shop.exception.EmptyFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileValidatorTest {

    private static final String FILE_TXT = "file.txt";
    private static final String UPLOAD_DIR_DOES_NOT_EXIST = "Upload directory does not exist: ";
    private static final String UPLOAD_DIR_IS_EMPTY = "Upload directory is empty: ";

    @InjectMocks
    private FileValidator fileValidator;

    @TempDir
    Path tempDir;

    private final Path nonExistentDir = Path.of("nonExistentDir");
    private File emptyDir;

    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        multipartFile = mock(MultipartFile.class);

        emptyDir = createDirectory("emptyDir");
        File validDir = createDirectory("validDir");

        createFileInDirectory(validDir);

        fileValidator = new FileValidator(validDir.getPath());
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        when(multipartFile.isEmpty()).thenReturn(true);

        assertThrows(EmptyFileException.class, () -> fileValidator.validateFileIsNotEmpty(multipartFile));
        verify(multipartFile, times(1)).isEmpty();
    }

    @Test
    void shouldNotThrowExceptionWhenFileIsNotEmpty() {
        when(multipartFile.isEmpty()).thenReturn(false);

        assertDoesNotThrow(() -> fileValidator.validateFileIsNotEmpty(multipartFile));
        verify(multipartFile, times(1)).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDirectoryDoesNotExist() {
        File nonExistentDirFile = tempDir.resolve(nonExistentDir).toFile();
        FileValidator fileValidator = new FileValidator(nonExistentDirFile.getPath());

        DirectoryCreationException exception = assertThrows(DirectoryCreationException.class, fileValidator::validateUploadDirectory);

        assertEquals(UPLOAD_DIR_DOES_NOT_EXIST + nonExistentDirFile.getPath(), exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenDirectoryIsEmpty() {
        FileValidator fileValidator = new FileValidator(emptyDir.getPath());

        DirectoryCreationException exception = assertThrows(DirectoryCreationException.class, fileValidator::validateUploadDirectory);

        assertEquals(UPLOAD_DIR_IS_EMPTY + emptyDir.getPath(), exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenDirectoryIsValid() {
        assertDoesNotThrow(() -> fileValidator.validateUploadDirectory());
    }

    private File createDirectory(String dirName) {
        File dir = tempDir.resolve(dirName).toFile();
        boolean dirCreated = dir.mkdir();
        assertTrue(dirCreated || dir.exists(), "Failed to create or find the directory: " + dirName);
        return dir;
    }

    private void createFileInDirectory(File dir) {
        File file = new File(dir, FILE_TXT);
        try {
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                assertTrue(fileCreated, "Failed to create file: " + FILE_TXT);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file: " + FILE_TXT, e);
        }
    }
}
