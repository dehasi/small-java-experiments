package lambda;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaTest {

    private final UnaryOperator<Integer> fib =
        n -> (n < 2) ? n : this.fib.apply(n - 1) + this.fib.apply(n - 2);

    private final Function<Integer, Integer> fb =
        n -> (n < 2) ? n : this.fb.apply(n - 1) + this.fb.apply(n - 2);

    @Test void t() {
        assertEquals(55, fib.apply(10));
    }

    @Test void t2() {
        assertEquals(55, fb.apply(10));
    }
}
