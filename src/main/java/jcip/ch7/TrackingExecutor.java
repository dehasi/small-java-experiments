package jcip.ch7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.synchronizedSet;

class TrackingExecutor extends AbstractExecutorService {

    private final Set<Runnable> taskCancelledAtShutdown = synchronizedSet(new HashSet<>());

    private final ExecutorService executorService;

    TrackingExecutor(ExecutorService service) {executorService = service;}

    @Override public void shutdown() { executorService.shutdown(); }

    @Override public List<Runnable> shutdownNow() { return executorService.shutdownNow(); }

    @Override public boolean isShutdown() { return executorService.isShutdown(); }

    @Override public boolean isTerminated() { return executorService.isTerminated(); }

    @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override public void execute(Runnable command) {
        executorService.execute(() -> {
            try {
                command.run();
            } finally {
                if (isShutdown() && Thread.currentThread().isInterrupted())
                    taskCancelledAtShutdown.add(command);
            }
        });
    }

    ArrayList<Runnable> getCancelledTasks() {
        assert isTerminated();
        return new ArrayList<>(taskCancelledAtShutdown);
    }
}
