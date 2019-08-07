package github.incodelearning.collections;

import org.junit.Before;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class StackTest {
    private Stack<Integer> tbt;

    @Before
    public void setup() {
        tbt = new Stack<>();
    }

    /**
     * One would assume the iteration order of a stack is the stack popping order. It is not!
     */
    @Test
    public void testIteration() {
        Integer[] nums = new Integer[] {1, 2, 3};
        for(Integer n: nums) tbt.push(n);
        int count = 0;
        for(Integer n: tbt) assertEquals(nums[count++], n);
    }

    @Test
    public void testAsStack() {
        Integer[] nums = new Integer[] {1, 2, 3};
        for(Integer n: nums) tbt.push(n);
        int count = 2;
        while (!tbt.isEmpty()) assertEquals(nums[count--], tbt.pop());
    }
}
