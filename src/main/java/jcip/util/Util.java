package jcip.util;

final public class Util {

    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException)t;
        else if (t instanceof Error)
            throw (Error)t;
        else
            return new IllegalStateException("Not unchecked", t);
    }
}
