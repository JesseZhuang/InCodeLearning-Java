package github.incodelearning.basics;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;



public class DequeTest {
    @Test
    public void testLinkedListAsStack() {
        Deque<Integer> stack = new LinkedList<>(); // can be declared as Deque or LinkedList
        List<Integer> list = Arrays.asList(1,2,3);
        for (int i: list) stack.push(i);
        assertEquals(Arrays.asList(3,2,1), stack);
    }
}
