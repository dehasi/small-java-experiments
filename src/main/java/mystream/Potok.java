package mystream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

class Potok<IN, OUT> {

    private final Spliterator<IN> spliterator;
    private final Function<IN, OUT> transform;

//    private Potok(Spliterator<IN> spliterator) {
//        this(spliterator, x -> x);
//    }

    private Potok(Spliterator<IN> spliterator, Function<IN, OUT> transform) {
        this.spliterator = spliterator;
        this.transform = transform;
    }

    @SafeVarargs
    static <TYPE> Potok<TYPE, TYPE> of(TYPE... elements) {
        return new Potok<>(new MyArraySpliterator<>(elements),  x -> x);
    }


//    <RESULT> Stream<RESULT> map(Function<? super IN, ? extends RESULT> mapper) {
//
//        return new Potok<IN, RESULT>(spliterator, transform.andThen(mapper););
//    }


    void forEach(Consumer<? super IN> consumer) {
        while (spliterator.tryAdvance(consumer)) ;
    }

    <RESULT, ACCUMULATOR> RESULT collect(Collector<? super OUT, ACCUMULATOR, RESULT> collector) {
        ACCUMULATOR accumulator = collector.supplier().get();
        spliterator.forEachRemaining(element -> collector.accumulator().accept(accumulator, transform.apply(element)));
        return collector.finisher().apply(accumulator);
    }

    public static void main(String[] args) {
        String collect = Potok.of(1, 2, 3, 4).collect(AndCollector.joinAnd());
        System.out.println(collect);
    }
}
