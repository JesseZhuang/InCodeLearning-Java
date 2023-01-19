package github.incodelearning.basics;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class StringTests {
    @Test
    public void testCodePoint() {
        String s = "Thanks \uD83D\uDE0A"; // last two char will print as ?
        System.out.println(s); // prints smiley face
        assertEquals(List.of(84, 104, 97, 110, 107, 115, 32, 128522),
                // last char codepoint 56842 not included because it is the second part of the 2-char unicode
                s.codePoints().boxed().collect(Collectors.toList()));
        assertEquals(List.of(1, 1, 1, 1, 1, 1, 1, 2), // 1 if codepoint < x10000
                s.codePoints().map(i -> Character.charCount(i)).boxed().collect(Collectors.toList()));
        final int charLimit = 0x10000; // char 2 bytes
        assertEquals(16, Math.log(charLimit) / Math.log(2), 0.00001);// math log is e based
        System.out.println(Character.toChars(charLimit)); // 𐀀
        System.out.println(Character.toChars(charLimit - 1)); // not visible
        System.out.println(Character.toChars(128522)); // smiley face
    }
}
