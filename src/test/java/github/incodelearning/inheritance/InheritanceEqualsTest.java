package github.incodelearning.inheritance;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InheritanceEqualsTest {
    @Test
    public void testParentEquals() {
        Parent p1 = new Parent("Tom" , 32, "test1");
        Parent p2 = new Parent("Tom" , 32, "test2");
        assertEquals(p1, p2);
    }

    @Test
    public void testChildEquals() {
        Child c1 = new Child("Raj", 13, "test3", "New School");
        Child c2 = new Child("Raj", 13, "test4", "Old School");
        assertEquals(c1, c2);
    }

}
