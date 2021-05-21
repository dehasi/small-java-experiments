package jcip.ch6.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Collections.emptyList;

class FutureRenderer {

    ExecutorService executorService = Executors.newFixedThreadPool(42);

    void renderPage(CharSequence source) {
        List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = () -> {
            List<ImageData> result = new ArrayList<>();
            for (ImageInfo imageInfo : imageInfos)
                result.add(imageInfo.downloadImage());
            return result;
        };
        Future<List<ImageData>> future = executorService.submit(task);
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
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
