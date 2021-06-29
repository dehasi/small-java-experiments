package leetcode.medium;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Task8 {
    Solution solution = new Solution();

    @Test void test1() {
        int result = solution.myAtoi("20000000000000000000");

        assertEquals(result, Integer.MAX_VALUE);
    }

    @Test void test2() {
        int result = solution.myAtoi("   -42");

        assertEquals(result, -42);
    }

    @Test void test3() {
        int result = solution.myAtoi("+-12");

        assertEquals(result, 0);
    }

    static class Solution {
        public int myAtoi(String s) {
            BigInteger min = BigInteger.valueOf(Integer.MIN_VALUE);
            BigInteger max = BigInteger.valueOf(Integer.MAX_VALUE);

            s = s.trim();
            if (s.isEmpty())
                return 0;
            int start = 0, end = 0, sign = 0;
            if (s.charAt(0) == '-' || s.charAt(0) == '+') {
                start = 1;
                ++sign;
            }
            for (end = start; end < s.length(); ++end) {
                char ch = s.charAt(end);
                if (!Character.isDigit(ch))
                    break;
            }
            if (end == start)
                return 0;
            if (sign > 0)
                --start;
            s = s.substring(start, end);
            if (s.isEmpty())
                return 0;

            BigInteger val = new BigInteger(s);
            if (val.compareTo(max) > 0)
                val = max;
            if (val.compareTo(min) < 0)
                val = min;

            return val.intValue();
        }
    }
}
