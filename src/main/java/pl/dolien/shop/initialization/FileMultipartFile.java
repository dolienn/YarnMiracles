package pl.dolien.shop.initialization;

import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileMultipartFile implements MultipartFile {
    private final File file;

    public FileMultipartFile(File file) {
        this.file = file;
    }

    @Override
    @Nonnull
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        return "application/octet-stream";
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    @Nonnull
    public byte[] getBytes() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        }
    }

    @Override
    @Nonnull
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dir) throws IOException, IllegalStateException {
        Files.copy(file.toPath(), dir.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
