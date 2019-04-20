package github.incodelearning.basics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DemoJDK12SwitchTest {
    @Test
    public void testFallThrough() {
        assertEquals(20, DemoJDK12Switch.calcHeight(DemoJDK12Switch.Size.S));
    }
    @Test
    public void testDefault() {
        assertEquals(0, DemoJDK12Switch.calcHeight(DemoJDK12Switch.Size.XL));
    }
    @Test
    public void testJDK12NoFallThrough() {
        assertEquals(18, DemoJDK12Switch.calcHeightSwitchExp(DemoJDK12Switch.Size.S));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testJDK12ThrowException() {
        DemoJDK12Switch.calcHeightSwitchExp(DemoJDK12Switch.Size.XL);
    }
}
