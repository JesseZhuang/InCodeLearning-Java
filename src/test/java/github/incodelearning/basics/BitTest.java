package github.incodelearning.basics;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class BitTest {
    @Test
    public void testNegate() {
        // negated = -original-1
        assertEquals(0xffff0000, ~0x0000ffff); // -65536, 65535
        assertEquals(0xffffffff, ~0x00000000); // -1, 0
        // negative indicates not found, bs returned -3, ~(-2) == 2 the index where 3 should be inserted
        assertEquals(2, ~Collections.binarySearch(Arrays.asList(1, 2, 4), 3));
        assertEquals(3, ~Collections.binarySearch(Arrays.asList(1, 2, 4), 6));
        assertEquals(0, ~Collections.binarySearch(Arrays.asList(1, 2, 4), -2));
        // found, return a non-negative index
        assertEquals(0, Collections.binarySearch(Arrays.asList(1, 2, 4), 1));
        // no guarantee which index for the duplicates will be returned
        assertEquals(1, Collections.binarySearch(Arrays.asList(1, 1, 2, 4), 1));
        assertEquals(2, Collections.binarySearch(Arrays.asList(1, 1, 1, 2, 4), 1));
    }

    @Test
    public void testJavaRightShifts() {
        assertEquals(-2, 0xfffffffe); // last byte: 1111 1110
        assertEquals(-1, -2 >> 1); // arithmetic (signed) shift
        assertEquals(2147483647, -2 >>> 1); // logical (unsigned) shift
        assertEquals(2147483647, 0x7fffffff); // first byte 0111 1111
    }

    @Test
    public void testRightMost1Bit() {
        int[] nums = {0b110, 0b10, 0b10100};
        int[] expected = {0b10, 0b10, 0b100};
        for (int i = 0; i < nums.length; i++) assertEquals(expected[i], nums[i] & -nums[i]);
    }
}
