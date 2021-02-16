package mystream;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collector;

class Potok<TYPE> {

    private final Spliterator<TYPE> spliterator;

    private Potok(Spliterator<TYPE> spliterator) {
        this.spliterator = spliterator;
    }

    @SafeVarargs
    static <TYPE> Potok<TYPE> of(TYPE... elements) {
        return new Potok<>(new MyArraySpliterator<>(elements));
    }

    void forEach(Consumer<? super TYPE> consumer) {
        while (spliterator.tryAdvance(consumer)) ;
    }

    <RESULT, ACCUMULATOR> RESULT collect(Collector<? super TYPE, ACCUMULATOR, RESULT> collector) {
        ACCUMULATOR accumulator = collector.supplier().get();
        spliterator.forEachRemaining(element -> collector.accumulator().accept(accumulator, element));
        return collector.finisher().apply(accumulator);
    }

    public static void main(String[] args) {
        String collect = Potok.of(1, 2, 3, 4).collect(AndCollector.joinAnd());
        System.out.println(collect);
    }
}
