package lambda;

import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaTest {

    private final UnaryOperator<Integer> fib =
            n -> (n < 2) ? n : this.fib.apply(n - 1) + this.fib.apply(n - 2);

    @Test void t() {
        assertEquals(55, fib.apply(10));
    }
}
