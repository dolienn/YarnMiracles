package pl.dolien.shop.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.DirectoryCreationException;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void validateFileIsNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
    }

    public String getUploadDirectory() {
        String absoluteUploadDir = new File(uploadDir).getAbsolutePath();
        ensureDirectoryExists(absoluteUploadDir);
        return absoluteUploadDir;
    }

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new DirectoryCreationException("Failed to create upload directory: " + directoryPath);
        }
    }
}

