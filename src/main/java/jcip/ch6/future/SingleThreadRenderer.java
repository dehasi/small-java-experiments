package jcip.ch6.future;

import java.util.ArrayList;
import java.util.List;

class SingleThreadRenderer {

    void renderPage(CharSequence source) {
        renderText(source);
        List<ImageData> imageData = new ArrayList<>();
        for (ImageInfo imageInfo : scanForImageInfo(source))
            imageData.add(imageInfo.downloadImage());
        for (ImageData data : imageData)
            renderImage(data);
    }

    private void renderImage(ImageData data) { }

    private ImageInfo[] scanForImageInfo(CharSequence source) {
        return new ImageInfo[0];
    }

    private void renderText(CharSequence source) { }

    static class ImageData {}

    static class ImageInfo {
        public ImageData downloadImage() {
            return null;
        }
    }
}
