package github.incodelearning.security;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static github.incodelearning.basics.Primitives.byteArrayToBinaryStrings;
import static org.junit.Assert.assertEquals;

public class Base16Test {
    @Test
    public void testApacheHex() {
        String input = "J"; // 0100 -> 4 1010 -> a
        String expected = "4a";
        System.out.println(String.format("input %s, bytes: %s; encoded %s, bytes: %s.",
                input, byteArrayToBinaryStrings(input.getBytes()),
                expected, byteArrayToBinaryStrings(expected.getBytes())));
        assertEquals(expected, new String(Hex.encodeHex(input.getBytes())));
    }
}
