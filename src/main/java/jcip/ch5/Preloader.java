package jcip.ch5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static jcip.util.Util.launderThrowable;

class Preloader {

    private final FutureTask<ProductInfo> futureTask = new FutureTask<>(this::loadProductInfo);
    private final Thread thread = new Thread(futureTask);

    public void start() { thread.start(); }

    public ProductInfo get() throws InterruptedException, DataLoadException {
        try {
            return futureTask.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException)cause;
            else
                throw launderThrowable(cause);
        }
    }

    private ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }

    static class ProductInfo {}

    static class DataLoadException extends Exception {}
}
