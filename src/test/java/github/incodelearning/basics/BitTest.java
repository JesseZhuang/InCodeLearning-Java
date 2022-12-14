package github.incodelearning.basics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BitTest {
    @Test
    public void testNegate() {
        // negated = -original-1
        assertEquals(0xffff0000, ~0x0000ffff); // -65536, 65535
        assertEquals(0xffffffff, ~0x00000000); // -1, 0
    }
}
