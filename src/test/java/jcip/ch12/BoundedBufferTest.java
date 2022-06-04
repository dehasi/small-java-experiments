package jcip.ch12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BoundedBufferTest {

    private static final long LOCKUP_DETECT_TIMEOUT = 1_000;

    private final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);

    @Test void isEmpty_created_true() {
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test void isEmpty_filled_false() throws InterruptedException {
        for (int i = 0; i < 10; ++i)
            bb.put(i);

        assertFalse(bb.isEmpty());
        assertTrue(bb.isFull());
    }

    @Test void take_empty_blocks() {
        Thread taker = new Thread(() -> {
            try {
                int unsend = bb.take();
                fail();
            } catch (InterruptedException success) {
                success.printStackTrace();
            }
        });

        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }
}