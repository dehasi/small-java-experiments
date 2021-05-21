package jcip.ch6.future;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Collections.emptyList;

class Renderer {

    private ExecutorService executorService = Executors.newFixedThreadPool(42);
    private CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executorService);

    void renderPage(CharSequence source) {
        List<ImageInfo> imageInfos = scanForImageInfo(source);

        for (ImageInfo imageInfo : imageInfos)
            completionService.submit(imageInfo::downloadImage);

        renderText(source);

        try {
            for (int t = 0; t < imageInfos.size(); t++) {
                Future<ImageData> future = completionService.take();
                ImageData imageData = future.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

    private RuntimeException launderThrowable(Throwable cause) {
        return new RuntimeException(cause);
    }

    private void renderImage(ImageData data) { }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return emptyList();
    }

    private void renderText(CharSequence source) { }

    static class ImageData {}

    static class ImageInfo {
        public ImageData downloadImage() {
            return null;
        }
    }
}
