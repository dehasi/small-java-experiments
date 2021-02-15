package mystream;

import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

class MyArraySpliterator<TYPE> implements Spliterator<TYPE> {

    private final TYPE[] array;
    private final int size;
    private int cur;

    @SafeVarargs
    static <TYPE> Stream<TYPE> of(TYPE... elements) {
        return StreamSupport.stream(new MyArraySpliterator<>(elements), false);
    }

    @SafeVarargs
    private MyArraySpliterator(TYPE... elements) {
        this.array = Objects.requireNonNull(elements);
        size = array.length;
        cur = 0;
    }

    private MyArraySpliterator(TYPE[] array, int size, int cur) {
        this.array = array;
        this.size = size;
        this.cur = cur;
    }

    @Override
    public boolean tryAdvance(Consumer<? super TYPE> consumer) {
        if (cur >= size) return false;
        consumer.accept(array[cur++]);
        return true;
    }

    @Override
    public Spliterator<TYPE> trySplit() {
        int len = size - cur;
        if (len <= 1) return null;

        int newSize = cur + len / 2;
        int newCur = cur;
        cur = len / 2;
        return new MyArraySpliterator<>(array, newSize, newCur);
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return SIZED;
    }

    public static void main(String[] args) {
        List<Integer> collect = MyArraySpliterator.of(1, 2, 3).collect(toList());
        System.out.println(collect);
        Spliterator<Integer> spliterator = MyArraySpliterator.of(1, 2, 3).spliterator();
        Spliterator<Integer> spliterator2 = spliterator.trySplit();
        while (spliterator.tryAdvance(System.out::println)) ;
        while (spliterator2.tryAdvance(System.err::println)) ;
    }
}
