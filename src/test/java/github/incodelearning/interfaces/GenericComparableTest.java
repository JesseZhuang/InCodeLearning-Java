package github.incodelearning.interfaces;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GenericComparableTest {
    @Test
    public void testCompare() {
        GenericComparable<Integer> genericComparable1 = new GenericComparable<>(2);
        GenericComparable<Integer> genericComparable2 = new GenericComparable<>(3);
        assertTrue(genericComparable1.key < genericComparable2.key);
        assertTrue(genericComparable1.key.compareTo(genericComparable2.key) < 0);
    }
}
