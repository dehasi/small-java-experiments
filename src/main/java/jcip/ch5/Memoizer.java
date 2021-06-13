package jcip.ch5;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static jcip.util.Util.launderThrowable;

class Memoizer<ARGUMENT, VALUE> implements Computable<ARGUMENT, VALUE> {

    private final ConcurrentHashMap<ARGUMENT, Future<VALUE>> cache = new ConcurrentHashMap<>();
    private final Computable<ARGUMENT, VALUE> computable;

    Memoizer(Computable<ARGUMENT, VALUE> computable) {this.computable = computable;}

    @Override public VALUE compute(ARGUMENT argument) {
        while (true) {
            Future<VALUE> future = cache.get(argument);
            if (future == null) {
                FutureTask<VALUE> futureTask = new FutureTask<>(() -> computable.compute(argument));
                future = cache.putIfAbsent(argument, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (CancellationException e) {
                cache.remove(argument, future);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }
}

interface Computable<ARGUMENT, VALUE> {
    VALUE compute(ARGUMENT argument) throws InterruptedException;
}