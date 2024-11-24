package pl.dolien.shop.initialization.imageUpdateStatus;

public interface ImageUpdateStatusService {

    ImageUpdateStatus getImageUpdateStatus();

    void updateImageStatus(ImageUpdateStatus imageUpdateStatus, boolean status);
}

