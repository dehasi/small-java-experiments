package leetcode.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Task860 {

    private final Solution solution = new Solution();

    @Test void ex1() {
        assertTrue(solution.lemonadeChange(new int[] {5, 5, 5, 10, 20}));
    }

    @Test void ex2() {
        assertTrue(solution.lemonadeChange(new int[] {5, 5, 10}));
    }

    @Test void ex3() {
        assertFalse(solution.lemonadeChange(new int[] {10, 10}));
    }

    @Test void ex4() {
        assertFalse(solution.lemonadeChange(new int[] {5, 5, 10, 10, 20}));
    }

    static class Solution {

        public boolean lemonadeChange(int[] bills) {
            int five = 0, ten = 0;
            for (int bill : bills) {
                switch (bill) {
                case 5:
                    ++five;
                    break;
                case 10:
                    if (five == 0)
                        return false;
                    --five;
                    ++ten;
                    break;
                case 20:
                    if (ten > 0 && five > 0) {
                        --five;
                        --ten;
                    } else if (five >= 3)
                        five -= 3;
                    else
                        return false;
                    break;
                default:
                    return false;
                }
            }
            return true;
        }
    }
}
