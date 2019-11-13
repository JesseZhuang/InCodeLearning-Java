package github.incodelearning.basics;

import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Primitives {

    /**
     * Convert a primitive byte to binary String.
     * <p>
     * 0xFF is 255, or 11111111 (max value for an unsigned byte). 0x100 is 256, or 100000000.
     * <p>
     * The & upcasts the byte to an integer. At that point, it can be anything from 0-255
     * (00000000 to 11111111, 0xFF mask excluded the leading 24 bits).
     * + 0x100 and .substring(1) ensure there will be leading zeroes.
     *
     * @return Binary representation of the byte as String.
     */
    public static String byteToBinaryString(byte b) {
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }

    public static List<String> byteArrayToBinaryStrings(byte[] bytes) {
        Byte[] boxed =  IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).toArray(Byte[]::new);
        return Stream.of(boxed).map(Primitives::byteToBinaryString).collect(Collectors.toList());
    }
}
