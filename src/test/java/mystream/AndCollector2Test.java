package mystream;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static mystream.AndCollector2.joinAnd;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AndCollector2Test {

    @Test
    void joinAnd_worksWithStrings() {
        assertEquals(Stream.of().collect(joinAnd()), "");
        assertEquals(Stream.of("a").collect(joinAnd()), "a");
        assertEquals(Stream.of("a", "b").collect(joinAnd(":", " or ")), "a or b");
        assertEquals(Stream.of("a", "b", "c").collect(joinAnd(": ", " or ")), "a: b or c");
        assertEquals(Stream.of("a", "b", "c").collect(joinAnd("; ")), "a; b and c");
    }

    @Test
    void joinAnd_worksWithInts() {
        assertEquals(Stream.of(1, 2, 3).collect(joinAnd()), "1, 2 and 3");
    }

    @Test
    void joinAnd_nulls() {
        assertEquals(Stream.of(null, null).collect(joinAnd()), "null and null");
    }

    @Test
    void joinAnd_worksInParallel() {
        String collect = IntStream.range(1, 100).boxed().map(String::valueOf).parallel().collect(joining(","));
        String collect1 = IntStream.range(1, 100).boxed().map(String::valueOf).parallel().collect(joinAnd(","));
        System.err.println(collect);
        System.err.println(collect1);
        // assertTrue(collect.endsWith(" and 100"));
    }
}
