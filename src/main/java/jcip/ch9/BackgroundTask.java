package jcip.ch9;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

abstract class BackgroundTask<V> implements Runnable, Future<V> {

    private final FutureTask<V> computation = new Computation();

    private class Computation extends FutureTask<V> {

        public Computation() {
            super(BackgroundTask.this::compute);
        }

        protected final void done() {
            Executors.newCachedThreadPool().execute(() -> {
                V value = null;
                Throwable thrown = null;
                boolean cancelled = false;

                try {
                    value = get();
                } catch (ExecutionException e) {
                    thrown = e.getCause();
                } catch (CancellationException e) {
                    cancelled = true;
                } catch (InterruptedException consumed) {
                } finally {
                    onCompletion(value, thrown, cancelled);
                }
            });
        }
    }

    protected void setProgress(int current, int max) {
        Executors.newCachedThreadPool().execute(() -> onProgress(current, max));
    }

    protected abstract V compute() throws Exception; // can call setProgress

    protected void onCompletion(V result, Throwable exception, boolean cancelled) {}

    protected void onProgress(int current, int max) {}
}
