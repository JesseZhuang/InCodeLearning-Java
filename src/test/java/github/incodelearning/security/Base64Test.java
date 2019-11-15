package github.incodelearning.security;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Base64;

import static github.incodelearning.basics.Primitives.byteArrayToBinaryStrings;
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

    /**
     * It uses the URL and Filename safe Base64 alphabet and does not add any line separation.
     */
    @Test
    public void testEncodeUrl() {
        String originalUrl = "https://www.google.co.nz/?gfe_rd=cr&ei=dzbFV&gws_rd=ssl#q=java";
        String encodedUrl = Base64.getUrlEncoder().encodeToString(originalUrl.getBytes());
        assertEquals("aHR0cHM6Ly93d3cuZ29vZ2xlLmNvLm56Lz9nZmVfcmQ9Y3ImZWk9ZHpiRlYmZ3dzX3JkPXNzbCNxPWphdmE=",
                encodedUrl);
        assertEquals(originalUrl, new String(Base64.getUrlDecoder().decode(encodedUrl)));
    }

    /**
     * The MIME encoder generates a Base64 encoded output using the basic alphabet but in a MIME friendly format:
     * each line of the output is no longer than 76 characters and ends with a carriage return
     * followed by a linefeed (\r\n).
     */
    @Test
    public void testMIME() {
        byte[] encodedAsBytes = ("Long test string more than 76 characters without any line break." +
                "Another line to fill more than 76 characters and test.").getBytes();
        String encodedMime = Base64.getMimeEncoder().encodeToString(encodedAsBytes);
        String expected = "TG9uZyB0ZXN0IHN0cmluZyBtb3JlIHRoYW4gNzYgY2hhcmFjdGVycyB3aXRob3V0IGFueSBsaW5l\r\n" +
                "IGJyZWFrLkFub3RoZXIgbGluZSB0byBmaWxsIG1vcmUgdGhhbiA3NiBjaGFyYWN0ZXJzIGFuZCB0\r\n" +
                "ZXN0Lg==";
        assertEquals(expected, encodedMime);
    }

    @Test
    public void testApache() {
        String originalInput = "test input";
        String expected = "dGVzdCBpbnB1dA==";
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        String encodedString = new String(base64.encode(originalInput.getBytes()));
        assertEquals(expected, encodedString);
        // alternatively static method
        String encodedString2 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(originalInput.getBytes()));
        assertEquals(encodedString, encodedString2);
        assertEquals(expected, Base64.getEncoder().encodeToString(originalInput.getBytes()));
    }


}
