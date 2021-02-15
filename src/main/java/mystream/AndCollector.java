package mystream;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

class AndCollector implements Collector<String, String, String> {

    private final String sep;
    private final String and;

    private String result;
    private String last;

    private AndCollector(String sep, String and) {
        this.sep = sep;
        this.and = and;
    }

    @Override
    public Supplier<String> supplier() {
        return () -> result;
    }

    @Override
    public BiConsumer<String, String> accumulator() {
        return (__, b) -> {
            if (result == null) {
                result = b;
                return;
            }
            if (last != null) result += sep + last;
            last = b;
        };
    }

    @Override
    public BinaryOperator<String> combiner() {
        return (a, b) -> a + b;
    }

    @Override
    public Function<String, String> finisher() {
        return (x) -> {
            if (last != null) return result + and + last;
            else return result;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.UNORDERED);
    }


    public static AndCollector joinAnd() {
        return joinAnd(", ");
    }

    public static AndCollector joinAnd(String sep) {
        return joinAnd(sep, " and ");
    }

    public static AndCollector joinAnd(String sep, String and) {
        return new AndCollector(sep, and);
    }

    public static void main(String[] args) {
        System.out.println(Stream.of("a").collect(joinAnd())); // a
        System.out.println(Stream.of("a", "b").collect(joinAnd(":", " or "))); // a or b
        System.out.println(Stream.of("a", "b", "c").collect(joinAnd(": ", " or "))); // a: b or c
        System.out.println(Stream.of("a", "b", "c").collect(joinAnd("; "))); // a; b and c
    }
}
