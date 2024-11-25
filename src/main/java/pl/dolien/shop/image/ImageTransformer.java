package pl.dolien.shop.image;

import java.awt.image.BufferedImage;

class ImageTransformer {

    private static final String PORTRAIT_TRANSFORMATION = "w_171,h_171,c_fill";
    private static final String LANDSCAPE_TRANSFORMATION = "w_171,h_171,c_pad,b_auto";

    static String getTransformation(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        return (height > width) ? PORTRAIT_TRANSFORMATION : LANDSCAPE_TRANSFORMATION;
    }
}
