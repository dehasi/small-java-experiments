package lambda;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;

class Unchecked {

    public static void main(String[] args) {

        Stream.of(new File("/"))
              .map(unchecked(File::getCanonicalPath))
              .forEach(System.out::println);
    }

    private static <IN, OUT> Function<IN, OUT> unchecked(CheckedFunction<IN, OUT> f) {
        return x -> {
            try {
                return f.apply(x);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    private interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
