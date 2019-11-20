package github.incodelearning.concurrency.oracle;

import lombok.AllArgsConstructor;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Knowledge points related to {@link java.util.concurrent.Callable}.
 */
public class DemoCallable {

    /**
     * {@link Runnable} can create a new thread but does not return a result. Callable can return a result and
     * throw an exception.
     */
    public static class BasicExample implements Callable<Integer> {
        public Integer call() throws Exception {
            // Create random number generator
            Random generator = new Random();

            Integer randomNumber = generator.nextInt(5);

            // To simulate a heavy computation,
            // we delay the thread for some random time
            Thread.sleep(randomNumber * 1000);

            return randomNumber;
        }
    }

    @AllArgsConstructor
    public static class FactorialCallable implements Callable<Integer> {
        int number;

        public Integer call() throws IllegalArgumentException {
            if (number < 0) throw new IllegalArgumentException("Cannot calculate factorial for negative number.");
            int fact = 1;
            for(int count = number; count > 1; count--) {
                fact = fact * count;
            }
            return fact;
        }
    }
}
