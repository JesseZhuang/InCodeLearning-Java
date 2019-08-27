package github.incodelearning.http;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseHttpClientTest {
    protected static final int REQUEST_COUNT = 10;
    protected static final String SEPARATOR = ",";
    protected static final AtomicInteger COUNTER = new AtomicInteger(0);
    protected static final StringBuilder EVERY_REQ_COST = new StringBuilder(200);
    protected static final String SITE_TO_TEST = "http://localhost:8000/test";

    /**
     * get REQUEST_COUNT threads list.
     */
    protected List<Thread> getRunThreads(Runnable runnable) {
        List<Thread> tList = new ArrayList<>(REQUEST_COUNT);
        for (int i = 0; i < REQUEST_COUNT; i++) {
            tList.add(new Thread(runnable));
        }
        return tList;
    }

    /**
     * start all threads
     */
    protected void startAllThreads(List<Thread> tList) {
        for (Thread t : tList) {
            t.start();
            try {
                Thread.sleep(300); // guarantee order
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void addCost(long cost) {
        EVERY_REQ_COST.append(cost);
        EVERY_REQ_COST.append("ms");
        EVERY_REQ_COST.append(SEPARATOR);
    }

}
