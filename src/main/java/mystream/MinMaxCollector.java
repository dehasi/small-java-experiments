package mystream;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;

class MinMaxCollector {

    static <T, R> Collector<T, ?, Optional<R>> minMax(
            Comparator<? super T> cmp,
            BiFunction<? super T, ? super T, R> finisher) {
        requireNonNull(cmp); requireNonNull(finisher);

        class Acc {
            T min, max;
            boolean present;

            void add(T t) {
                if (present) {
                    if (cmp.compare(t, min) < 0) min = t;
                    if (cmp.compare(t, max) > 0) max = t;
                } else {
                    min = max = t;
                    present = true;
                }
            }

            Acc combine(Acc other) {
                if (!other.present) return this;
                if (!present) return other;
                if (cmp.compare(other.min, min) < 0) min = other.min;
                if (cmp.compare(other.max, max) > 0) max = other.max;
                return this;
            }
        }
        return Collector.of(Acc::new, Acc::add, Acc::combine,
                acc -> acc.present ? Optional.of(finisher.apply(acc.min, acc.max)) :
                        Optional.empty());
    }
}
