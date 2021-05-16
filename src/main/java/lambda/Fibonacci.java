package lambda;

import java.util.function.UnaryOperator;

/**
 * Recursive calculation of Fibonacci and Factorial using λ.
 * The only thing you cannot do with an anonymous function is recursion, exactly because you have no name to use for the recursive call.
 * Provided that the recursive call uses a name defined in the enclosing environment of the lambda (as a static instance variable) this is feasible.
 * This means that recursive definitions can only be made in the context of variable assignment and,
 * in fact—given the assignment-before-use rule for local variables—only of instance or static variable assignment.
 */
public class Fibonacci {
    private static UnaryOperator<Integer> factorial;
    private static UnaryOperator<Integer> fib;

    public static void main(String[] args) {
        fib = n -> {
            return n == 0 || n == 1 || n == 2 ? 1 : fib.apply(n - 1) + fib.apply(n - 2);
        };
        System.out.println(fib.apply(10));          // 55
        factorial = i -> {
            return i == 0 ? 1 : i * factorial.apply(i - 1);
        };
        System.out.println(factorial.apply(10));    // 3628800
    }
}
