package github.incodelearning.concurrency.oracle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DemoCallableTest {

    DemoCallable.BasicExample basicExample;

    @Before
    public void initialize() {
        basicExample = new DemoCallable.BasicExample();
        System.out.println("initialized Callable  " + basicExample);
    }

    @Test
    public void testCallableResultInRange() throws Exception {
        // a FutureTask implements RunnableFuture, which extends Runnable and Future
        FutureTask<Integer> randomNumberTask = new FutureTask<>(basicExample);
        Thread t = new Thread(randomNumberTask);
        t.start();
        Integer result = randomNumberTask.get();
        Assert.assertTrue(result < 5 && result >= 0);
    }

    @Test
    public void testCallableSleepTimeInRange() throws Exception {
        FutureTask<Integer> randomNumberTask = new FutureTask<>(basicExample);
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(randomNumberTask);
        t.start();
        Integer result = randomNumberTask.get();
        System.out.println("result " + result);
        long endTime = System.currentTimeMillis();
        double time = (endTime - startTime) / 1000d;
        System.out.println("time " + time);
        Assert.assertTrue(time >= 0L && time < 5L);
    }

    @Test
    public void whenException_ThenCallableThrowsIt() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        DemoCallable.FactorialCallable callable = new DemoCallable.FactorialCallable(-5);
        Future<Integer> future = executorService.submit(callable);

        try {
            future.get().intValue();
        } catch (ExecutionException e) {
            assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    @Test
    public void whenException_ThenCallableDoesntThrowsItIfGetIsNotCalled(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        DemoCallable.FactorialCallable task = new DemoCallable.FactorialCallable(-5);
        Future<Integer> future = executorService.submit(task);

        assertEquals(false, future.isDone());
    }
}
