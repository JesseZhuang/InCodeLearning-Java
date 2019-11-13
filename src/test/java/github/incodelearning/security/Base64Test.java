package github.incodelearning.security;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import org.junit.Test;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static github.incodelearning.basics.Primitives.byteArrayToBinaryStrings;
import static github.incodelearning.basics.Primitives.byteToBinaryString;
import static org.junit.Assert.assertEquals;

public class Base64Test {
    /**
     * The output is mapped to a set of characters in A-Za-z0-9+/ (a total of 64 chars).
     * <p>
     * The length of output encoded String must be a multiple of 3. If it's not, the output will be padded with
     * additional pad characters (`=`). On decoding, these extra padding characters will be discarded.
     * See <a href="https://stackoverflow.com/questions/4080988/why-does-base64-encoding-require-padding-if-the-input-length-is-not-divisible-by/18518605#18518605">about padding</a>.
     * <p>
     * Base256 fits exactly into this paradigm. One byte is equal to one character in base256.
     * <p>
     * Base16, hexadecimal or hex, uses 4 bits for each character. One byte can represent two base16 characters.
     * <p>
     * Base64 does not fit evenly into the byte paradigm, unlike base256 and base16. All base64 characters can be
     * represented in 6 bits, 2 bits short of a full byte. We can represent base64 encoding versus the byte paradigm
     * as a fraction: 6 bits per character over 8 bits per byte. Reduced this fraction is
     * 3 bytes over 4 base64 characters.
     * <p>
     * See <a href="https://stackoverflow.com/questions/3538021/why-do-we-use-base64">why base64</a>. When you
     * encode text in ASCII, you start with a text string and convert it to a sequence of bytes (text -> binary).
     * When you encode data in Base64, you start with a sequence of bytes and convert it to a text string
     * (binary -> text). An example usage is to encode binary data in XML, e.g.,
     * <pre>
     * {@code
     * <images>
     *     <image name="Sally" encoding="base64">j23894uaiAJSD3234kljasjkSD...</image>
     * </images>}
     * </pre>
     */
    @Test
    public void testEncode() {
        String[] input = {"", "f", "fo", "foo", "foob", "fooba", "foobar"};
        String[] expected = {"", "Zg==", "Zm8=", "Zm9v", "Zm9vYg==", "Zm9vYmE=", "Zm9vYmFy"};
        for (int i = 0; i < input.length; i++) {
            System.out.println(String.format("input %s, bytes: %s; encoded %s, bytes: %s.",
                    input[i], byteArrayToBinaryStrings(input[i].getBytes()),
                    expected[i], byteArrayToBinaryStrings(expected[i].getBytes())));
            assertEquals(expected[i], Base64.getEncoder().encodeToString(input[i].getBytes()));
        }
    }

    @Test
    public void testPadding() {
        String input = "f";
        assertEquals(ImmutableList.of("01100110"), byteArrayToBinaryStrings(input.getBytes()));
        // f: 0110 0110 (0x66) -> 011001 (Z, index 25), 1000000 (g, index 32), padding 0x3D (61), padding 0x3D (61)
        // Z -> 90, 0x5A, 0b01011010
        String expected = "Zg";
        String unPaddedEncoded = Base64.getEncoder().withoutPadding().encodeToString(input.getBytes());
        assertEquals(expected, unPaddedEncoded);
        // encoded uses ascii representation
        assertEquals('Z', '\u005A');
        assertEquals('Z', 0x5A);
        assertEquals('Z', 0b01011010);
        assertEquals('Z', 90);
        assertEquals('g', 103);
        assertEquals('g', 0x67);
        assertEquals('g', 0b01100111);
        assertEquals(ImmutableList.of("01011010", "01100111"), byteArrayToBinaryStrings(unPaddedEncoded.getBytes()));
    }
}
