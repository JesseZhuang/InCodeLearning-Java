package github.incodelearning.basics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DemoEnumTest {

    static DemoEnum.DayOfWeek tbt = DemoEnum.DayOfWeek.MONDAY;
    private static final String MONDAY = "MONDAY";
    private static final String MONDAY_CAMEL = "Monday";

    @Test
    public void testEnumToString() {
        assertEquals(MONDAY_CAMEL, tbt.toString());
    }

    @Test
    public void testEnumName() {
        assertEquals(MONDAY, tbt.name());
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

    @Test
    public void testEnumNoOverrideToString() {
        assertEquals(MONDAY, DemoEnum.DayOfWeekNoToStringOverride.MONDAY.toString());
        assertEquals(MONDAY, DemoEnum.DayOfWeekNoToStringOverride.MONDAY.name());
    }

    @Test
    public void testValueOf() {
        assertEquals(DemoEnum.DayOfWeek.MONDAY, DemoEnum.DayOfWeek.valueOf(MONDAY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOfUnexpectedShouldThrowException() {
        DemoEnum.DayOfWeek.valueOf(MONDAY_CAMEL);
    }
}
