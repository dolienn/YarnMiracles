package pl.dolien.shop.image;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageTransformer {

    private static final String PORTRAIT_TRANSFORMATION = "w_171,h_171,c_fill";
    private static final String LANDSCAPE_TRANSFORMATION = "w_171,h_171,c_pad,b_auto";

    public String determineTransformation(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        if (height > width) {
            return PORTRAIT_TRANSFORMATION;
        } else {
            return LANDSCAPE_TRANSFORMATION;
        }
    }
}
