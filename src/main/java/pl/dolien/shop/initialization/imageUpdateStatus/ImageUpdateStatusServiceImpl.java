package pl.dolien.shop.initialization.imageUpdateStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUpdateStatusServiceImpl implements ImageUpdateStatusService {

    private final ImageUpdateStatusRepository imageUpdateStatusRepository;

    @Override
    public ImageUpdateStatus getImageUpdateStatus() {
        List<ImageUpdateStatus> statusList = imageUpdateStatusRepository.findAll();
        return statusList.isEmpty() ? new ImageUpdateStatus() : statusList.get(0);
    }

    @Override
    public void updateImageStatus(ImageUpdateStatus imageUpdateStatus, boolean status) {
        imageUpdateStatus.setImagesUpdated(status);
        saveImageUpdateStatus(imageUpdateStatus);
    }

    private void saveImageUpdateStatus(ImageUpdateStatus imageUpdateStatus) {
        imageUpdateStatusRepository.save(imageUpdateStatus);
    }
}