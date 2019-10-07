package github.incodelearning.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

public class HttpClientWithoutPoolTest extends BaseHttpClientTest {

    //126ms,492ms,493ms,111ms,102ms,112ms,100ms,599ms,119ms,116ms,129ms,108ms,110ms,100ms,
    //126ms,101ms,112ms,102ms,113ms,100ms,98ms,98ms,97ms,100ms,102ms,106ms,98ms,90ms,120ms,98ms,
    //localhost 196ms,196ms,197ms,4ms,2ms,3ms,2ms,2ms,2ms,4ms,5ms,1ms,2ms,3ms,2ms,7ms,2ms,
    //3ms,2ms,4ms,3ms,2ms,2ms,2ms,2ms,2ms,2ms,1ms,2ms,2ms,

    /**
     * <pre>
     * >$ netstat -an | grep 8000
     *    tcp46      0      0  *.8000                 *.*                    LISTEN
     *    tcp4       0      0  127.0.0.1.49487        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49488        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49489        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49492        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49493        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49494        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49495        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49496        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49497        127.0.0.1.8000         TIME_WAIT
     *    tcp4       0      0  127.0.0.1.49498        127.0.0.1.8000         TIME_WAIT
     * </pre>
     */
    public void test() {
        startAllThreads(getRunThreads(new HttpThread()));
        for (; ; ) ; // wait for threads to run
    }

    private class HttpThread implements Runnable {
        @Override
        public void run() {
            /**
             * HttpClient is thread safe. When used as a field，HttpClient will construct a connection pool.
             * Here every new thread uses a different instance of the client to force no connection pool.
             */
            CloseableHttpClient httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(SITE_TO_TEST);
            httpGet.setHeader(new BasicHeader("Cache-Control", "no-cache"));
            long startTime = System.currentTimeMillis();
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                addCost(System.currentTimeMillis() - startTime);
                if (COUNTER.incrementAndGet() == REQUEST_COUNT) {
                    System.out.println(EVERY_REQ_COST.toString());
                }
            }
        }

    }
}
