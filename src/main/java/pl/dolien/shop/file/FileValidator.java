package pl.dolien.shop.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.DirectoryCreationException;
import pl.dolien.shop.exception.EmptyFileException;

import java.io.File;

@Component
public class FileValidator {

    private final File directory;

    public FileValidator(@Value("${file.upload-dir}") String uploadDirPath) {
        this.directory = new File(uploadDirPath);
    }

    public void validateFileIsNotEmpty(MultipartFile file) {
        if (file.isEmpty())
            throw new EmptyFileException("File cannot be empty");
    }

    public void validateUploadDirectory() {
        if (!directory.exists() || !directory.isDirectory())
            throw new DirectoryCreationException("Upload directory does not exist: " + directory.getPath());

        File[] files = directory.listFiles();

        if (files == null || files.length == 0)
            throw new DirectoryCreationException("Upload directory is empty: " + directory.getPath());
    }
}

