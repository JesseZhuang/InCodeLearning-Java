package github.incodelearning.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    private ArrayDeque<Integer> tbt;

    @Before
    public void setup() {
        tbt = new ArrayDeque<>();
    }

    @Test
    public void testArrayDequeAsQueue() {
        Integer[] nums = new Integer[] {1, 2, 3};
        for (int i = 0; i < nums.length; i++) tbt.add(nums[i]);
        int count = 0;
        for (Integer i : tbt) assertEquals(nums[count++], i);
        count = 0;
        while (!tbt.isEmpty()) assertEquals(nums[count++], tbt.remove());
    }
}
