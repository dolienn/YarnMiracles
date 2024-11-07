package pl.dolien.shop.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.DirectoryCreationException;
import pl.dolien.shop.exception.EmptyFileException;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void validateFileIsNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("File cannot be empty");
        }
    }

    public void validateUploadDirectory() {
        File directory = new File(uploadDir);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new DirectoryCreationException("Upload directory does not exist: " + uploadDir);
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new DirectoryCreationException("Upload directory is empty: " + uploadDir);
        }
    }
}

