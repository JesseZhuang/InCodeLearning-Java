package github.incodelearning.basics;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DemoEnumTest {

    static DemoEnum.DayOfWeek tbt = DemoEnum.DayOfWeek.MONDAY;

    @Test
    public void testEnumToString() {
        assertEquals("Monday", tbt.toString());
    }

    @Test
    public void testEnumName() {
        assertEquals("MONDAY", tbt.name());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnumName() {
        DemoEnum.DayOfWeek day = null;
        System.out.println(day.name());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnumToString() {
        DemoEnum.DayOfWeek day = null;
        System.out.println(day.toString());
    }

    @Test
    public void testNullEnumPrintNoNPE() {
        DemoEnum.DayOfWeek day = null;
        System.out.println("day: " + day);
    }
}
