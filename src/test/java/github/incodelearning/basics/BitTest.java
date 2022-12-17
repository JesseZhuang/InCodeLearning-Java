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
}
