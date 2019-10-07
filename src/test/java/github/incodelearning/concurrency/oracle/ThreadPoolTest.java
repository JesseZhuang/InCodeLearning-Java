package github.incodelearning.concurrency.oracle;

import lombok.AllArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Test 1000 tasks (threads) (each task increment an AtomicInteger a certain times) with and without thread pool.
 * <p>
 * When task is simple, e.g., INCREMENT_TIMES = 10, main thread cost is less when using thread pool because
 * tasks do not have to spend time waiting (being blocked) in the 100 threads pool. It takes less time to reuse
 * the 100 threads comparing to creating 1000 new threads.
 * <ul>
 * <li>without thread pool, total time cost for all threads (30 ms) is less than
 * main thread time cost (150 ms) because main thread spends a lot of time creating the 1000 threads.
 * <li>with thread pool, total time cost for all threads (1 ms) and main thread cost is 30 ms.
 * <p>
 * When task is big, e.g. INCREMENT_TIMES = 1000000, main thread cost is less without thread pool.
 * <ul>
 * <li>without thread pool, main thread cost 3400 ms, total threads cost 20,000 ms.
 * <li>with 100 thread pool, main thread cost 12,000 ms, total threads cost 1,000,000 ms.
 * <li>with 500 thread pool, 4 iterations main thread cost 4500 ms and threads cost 350,000 ms then
 * java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
 */
public class ThreadPoolTest {
    private static final int TASK_COUNT = 1000;
    private static final int INCREMENT_TIMES = 1000000;

    @AllArgsConstructor
    class TaskMetrics {
        final long startTime;
        AtomicInteger finishedCount;
        AtomicLong timeCost;
    }

    public void testRunWithoutThreadPool() {
        for (int i = 0; i < 10; i++) {
            TaskMetrics taskMetrics = new TaskMetrics(System.currentTimeMillis(), new AtomicInteger(0),
                    new AtomicLong(0));
//            System.out.println("iteration " + i + " " + taskMetrics + "["
//                    + taskMetrics.finishedCount.get() + "," + taskMetrics.timeCost.get() + "]");
            List<Thread> tList = new ArrayList<>(TASK_COUNT);
            for (int j = 0; j < TASK_COUNT; j++) tList.add(new Thread(new IncreaseThread(taskMetrics)));
            for (Thread t : tList) t.start();
        }
        for (; ; ) ;
    }

    public void testRunWithThreadPool() {
        for (int i = 0; i < 10; i++) {
            TaskMetrics taskMetrics = new TaskMetrics(System.currentTimeMillis(), new AtomicInteger(0),
                    new AtomicLong(0));
            ThreadPoolExecutor executor = new ThreadPoolExecutor(500, 500, 0,
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            for (int j = 0; j < TASK_COUNT; j++) executor.submit(new IncreaseThread(taskMetrics));
        }
        for (; ; ) ;
    }

    @AllArgsConstructor
    class IncreaseThread implements Runnable {

        TaskMetrics taskMetrics;

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.incrementAndGet();
            }
            // latency
            taskMetrics.timeCost.addAndGet(System.currentTimeMillis() - startTime);
            if (taskMetrics.finishedCount.incrementAndGet() == TASK_COUNT) {
                System.out.println("threads cost: " + taskMetrics.timeCost.get() + " ms " + taskMetrics);
                System.out.println("main thead cost: " + (System.currentTimeMillis() - taskMetrics.startTime) + " ms");
            }
        }
    }

    @AllArgsConstructor
    class IncreaseTask implements Callable<List<Long>> {

        TaskMetrics taskMetrics;

        @Override
        public List<Long> call() {
            long startTime = System.currentTimeMillis();

            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.incrementAndGet();
            }
            // latency
            taskMetrics.timeCost.addAndGet(System.currentTimeMillis() - startTime);
            List<Long> costs = new ArrayList<>();
            if (taskMetrics.finishedCount.incrementAndGet() == TASK_COUNT) {
                costs.add(taskMetrics.timeCost.get());
                System.out.println("threads cost: " + taskMetrics.timeCost.get() + " ms " + taskMetrics);
                long latency = System.currentTimeMillis() - taskMetrics.startTime;
                costs.add(latency);
                System.out.println("main thead cost: " + latency + " ms");
            }
            return costs;
        }
    }

}
