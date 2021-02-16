package mystream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Collections.emptySet;

class AndCollector2<TYPE> implements Collector<TYPE, AndCollector2.Accumulator, String> {

    private final String sep;
    private final String and;

    private AndCollector2(String sep, String and) {
        this.sep = sep;
        this.and = and;
    }

    @Override
    public Supplier<AndCollector2.Accumulator> supplier() {
        return () -> new AndCollector2.Accumulator(sep, and);
    }

    @Override
    public BiConsumer<AndCollector2.Accumulator, TYPE> accumulator() {
        return Accumulator::append;
    }

    @Override
    public BinaryOperator<AndCollector2.Accumulator> combiner() {
        return AndCollector2.Accumulator::merge;
    }

    @Override
    public Function<AndCollector2.Accumulator, String> finisher() {
        return Accumulator::finish;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return emptySet();
    }

    public static <TYPE> AndCollector2<TYPE> joinAnd() {
        return joinAnd(", ");
    }

    public static <TYPE> AndCollector2<TYPE> joinAnd(String sep) {
        return joinAnd(sep, " and ");
    }

    public static <TYPE> AndCollector2<TYPE> joinAnd(String sep, String and) {
        return new AndCollector2<>(sep, and);
    }

    static class Accumulator {

        private final String sep;
        private final String and;

        StringBuilder result = new StringBuilder();
        private String last;

        Accumulator(String sep, String and) {
            this.sep = sep;
            this.and = and;
        }

        Accumulator merge(Accumulator accumulator) {
            result.append(last).append(accumulator.result);
            this.last = accumulator.last;
            return this;
        }

        void append(Object o) {
            if (result.length() == 0) {
                result.append(String.format("%s", o));
                return;
            }
            if (last != null) result.append(sep).append(last);

            last = String.format("%s", o);
        }

        String finish() {
            if (last != null) result.append(and).append(last);
            return result.toString();
        }

    }
}
