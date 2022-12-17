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
        assertEquals(2, ~Collections.binarySearch(Arrays.asList(1, 2, 4), 3));
    }

    @Test
    public void testJavaRightShifts() {
        assertEquals(-2, 0xfffffffe); // last byte: 1111 1110
        assertEquals(-1, -2 >> 1); // arithmetic (signed) shift
        assertEquals(2147483647, -2 >>> 1); // logical (unsigned) shift
        assertEquals(2147483647, 0x7fffffff); // first byte 0111 1111
    }
}
