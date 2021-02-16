package mystream;

import java.math.BigDecimal;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;

class Potok<IN, OUT> {

    private final Spliterator<IN> spliterator;
    private final Function<IN, OUT> transform;

    private Potok(Spliterator<IN> spliterator, Function<IN, OUT> transform) {
        this.spliterator = spliterator;
        this.transform = transform;
    }

    @SafeVarargs
    static <TYPE> Potok<TYPE, TYPE> of(TYPE... elements) {
        return new Potok<>(new MyArraySpliterator<>(elements), x -> x);
    }

    <RES> Potok<IN, RES> map(Function<OUT, RES> mapper) {
        Function<IN, RES> result = transform.andThen(mapper);
        return new Potok<>(spliterator, result);
    }

    void forEach(Consumer<? super IN> consumer) {
        while (spliterator.tryAdvance(consumer)) ;
    }

    <RESULT, ACCUMULATOR> RESULT collect(Collector<? super OUT, ACCUMULATOR, RESULT> collector) {
        ACCUMULATOR accumulator = collector.supplier().get();
        spliterator.forEachRemaining(element -> collector.accumulator().accept(accumulator, transform.apply(element)));
        return collector.finisher().apply(accumulator);
    }

    public static void main(String[] args) {
        String result = Potok.of(1, 2, 3, 4)
                             .map(x -> x + 2d)
                             .map(BigDecimal::new)
                             .collect(AndCollector.joinAnd());
        System.out.println(result);
    }
}
