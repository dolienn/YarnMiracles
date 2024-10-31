package pl.dolien.shop.initialization.imageUpdateStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUpdateStatusService {

    private final ImageUpdateStatusRepository imageUpdateStatusRepository;

    public ImageUpdateStatus getImageUpdateStatus() {
        List<ImageUpdateStatus> statusList = imageUpdateStatusRepository.findAll();
        return statusList.isEmpty() ? new ImageUpdateStatus() : statusList.get(0);
    }

    public void setImagesUpdated(boolean status) {
        ImageUpdateStatus imageUpdateStatus = getImageUpdateStatus();
        imageUpdateStatus.setImagesUpdated(status);
        imageUpdateStatusRepository.save(imageUpdateStatus);
    }

    public void saveImageUpdateStatus(ImageUpdateStatus imageUpdateStatus) {
        imageUpdateStatusRepository.save(imageUpdateStatus);
    }
}