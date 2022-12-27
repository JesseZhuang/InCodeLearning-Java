package github.incodelearning.functional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class SplitIteratorTest {
    @Test
    public void testSplitIterator() {
        List<Integer> ints = DemoStreamOps.generateNaturalNumbers(1, 1_000_001);
        int sum = ints.stream().mapToInt(Integer::intValue).sum(); // not overflow yet
        Spliterator<Integer> iterator = ints.spliterator();
        Spliterator<Integer> iterator2 = iterator.trySplit();
        Spliterator<Integer> iterator3 = iterator.trySplit();
        assertEquals(250_000, iterator.estimateSize());
        assertEquals(250_000, iterator3.estimateSize());
        assertEquals(500_000, iterator2.estimateSize());

        ForkJoinPool pool = new ForkJoinPool();
        List<Spliterator<Integer>> iterators = List.of(iterator, iterator2, iterator3);
        List<Callable<Integer>> callables = new ArrayList<>();
        for (Spliterator<Integer> it : iterators) {
            Callable<Integer> callable = () -> {
                AtomicInteger sum1 = new AtomicInteger(0);
                it.forEachRemaining(num -> sum1.getAndAdd(num));
                return sum1.get();
            };
            callables.add(callable);
        }
        List<Future<Integer>> results = pool.invokeAll(callables);
        assertEquals(sum, results.stream().mapToInt(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                System.out.println("concurrent exception: " + e);
                return 0;
            }
        }).sum());
    }
}
