package leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Task1758 {

    private final Solution solution = new Solution();

    @Test
    void example1() {
        int operations = solution.minOperations("0100");
        assertEquals(operations, 1);
    }

    @Test
    void example2() {
        int operations = solution.minOperations("10");
        assertEquals(operations, 0);
    }

    @Test
    void example3() {
        int operations = solution.minOperations("1111");
        assertEquals(operations, 2);
    }

    @Test
    void example4() {
        int operations = solution.minOperations("10010100");
        assertEquals(operations, 3);
    }

    static class Solution {
        public int minOperations(String s) {
            if (s.length() <= 1)
                return 0;
            int inv = count(s.toCharArray());
            char[] array = s.toCharArray();
            array[0] = invert(array[0]);

            return Math.min(inv, count(array) + 1);
        }

        private int count(char[] array) {
            char prev = array[0];
            int inv = 0;
            for (int i = 1; i < array.length; i++) {
                if (array[i] == prev) {
                    ++inv;
                    array[i] = invert(array[i]);
                }
                prev = array[i];
            }
            return inv;
        }

        private static char invert(char c) {
            return c == '0' ? '1' : '0';
        }
    }
}
