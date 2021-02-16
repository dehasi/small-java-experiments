package mystream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collector.Characteristics.UNORDERED;

class AndCollector<TYPE> implements Collector<TYPE, StringBuilder, String> {

    private final String sep;
    private final String and;

    private String last;

    private AndCollector(String sep, String and) {
        this.sep = sep;
        this.and = and;
    }

    @Override
    public Supplier<StringBuilder> supplier() {
        return StringBuilder::new;
    }

    @Override
    public BiConsumer<StringBuilder, TYPE> accumulator() {
        return (result, cur) -> {
            if (result.length() == 0) {
                result.append(String.format("%s", cur));
                return;
            }
            if (last != null) result.append(sep).append(last);

            last = String.format("%s", cur);
        };
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        return (left, right) -> {
            throw new RuntimeException(String.format("combiner was called left:%s, right:%s", left, right));
        };
    }

    @Override
    public Function<StringBuilder, String> finisher() {
        return (result) -> {
            if (last != null) result.append(and).append(last);
            return result.toString();
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return emptySet();
    }

    public static <TYPE> AndCollector<TYPE> joinAnd() {
        return joinAnd(", ");
    }

    public static <TYPE> AndCollector<TYPE> joinAnd(String sep) {
        return joinAnd(sep, " and ");
    }

    public static <TYPE> AndCollector<TYPE> joinAnd(String sep, String and) {
        return new AndCollector<>(sep, and);
    }
}
