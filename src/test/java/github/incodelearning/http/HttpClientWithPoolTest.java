package github.incodelearning.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * The time to create http connections might be negligible comparing to the http get latency.
 */
public class HttpClientWithPoolTest extends BaseHttpClientTest {
    private CloseableHttpClient httpClient = null;

    @Before
    public void before() {
        initHttpClient();
    }

    /**
     * <pre>
     * netstat -an | grep 8000
     * tcp4       0      0  127.0.0.1.8000         127.0.0.1.50686        ESTABLISHED
     * tcp4       0      0  127.0.0.1.50686        127.0.0.1.8000         ESTABLISHED
     * tcp46      0      0  *.8000                 *.*                    LISTEN
     * </pre>
     * <pre>
     * netstat -an | grep 8000
     * tcp4       0      0  127.0.0.1.8000         127.0.0.1.51688        ESTABLISHED
     * tcp4       0      0  127.0.0.1.51688        127.0.0.1.8000         ESTABLISHED
     * tcp46      0      0  *.8000                 *.*                    LISTEN
     * tcp4       0      0  127.0.0.1.51672        127.0.0.1.8000         TIME_WAIT
     * </pre>
     */
    @Test
    public void correctTest() throws InterruptedException {
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setDefaultMaxPerRoute(1);
        pool.setMaxTotal(1);
        httpClient = HttpClients.custom().setConnectionManager(pool).build();

        final String url = "http://localhost:8000/test";
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    HttpGet httpGet = new HttpGet(url);
                    CloseableHttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    //System.out.println(entity.getContent()); // not good, need to consume content
                    //System.out.println(new String(entity.getContent().readAllBytes()));// ok
                    EntityUtils.consume(entity);
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(100000);
    }

    //605ms,230ms,313ms,125ms,125ms,139ms,107ms,126ms,97ms,97ms,113ms,114ms,121ms,122ms,
    //104ms,103ms,115ms,99ms,105ms,105ms,107ms,100ms,97ms,101ms,136ms,96ms,112ms,95ms,95ms,102ms,

    //localhost 109ms,3ms,2ms,3ms,3ms,4ms,2ms,2ms,3ms,3ms,3ms,11ms,4ms,2ms,2ms,2ms,2ms,1ms,2ms,3ms,
    //3ms,2ms,3ms,4ms,4ms,3ms,2ms,2ms,3ms,3ms,

    /**
     * <pre></pre>
     * netstat -an | grep 8000
     * tcp46      0      0  *.8000                 *.*                    LISTEN
     * tcp4       0      0  127.0.0.1.50771        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50773        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50774        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50775        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50776        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50777        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50778        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50779        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50780        127.0.0.1.8000         TIME_WAIT
     * tcp4       0      0  127.0.0.1.50781        127.0.0.1.8000         TIME_WAIT
     * </pre>
     * <p>
     * Note listen state is all in TIME_WAIT. Once the HttpResponse is received,
     * in order to reuse and maintain the created connection it is necessary to close only
     * the InputStream (en.getContent()) on the http-response.
     * Do not call any other release method on HttpPost or Client objects.
     * Must read the input steam. Close the response directly will result in not reusing connections.
     */
    @Test
    public void test() {
        List<Thread> threads = getRunThreads(new HttpThread());
        startAllThreads(threads);
        for (; ; ) ;
    }

    private class HttpThread implements Runnable {
        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(SITE_TO_TEST);
            // optional，HTTP1.1 default is Connection: keep-alive
            httpGet.addHeader("Connection", "keep-alive");
//            httpGet.setHeader(new BasicHeader("Cache-Control", "no-cache"));
            long startTime = System.currentTimeMillis();
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response != null) {
                    System.out.println("response not null, closing without checking response content.");
                    response.close();
//                    response.getEntity().getContent().close(); correct way to close
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

    private void initHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1);
        connectionManager.setDefaultMaxPerRoute(1);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")), 1);
        // setConnectTimeout: time to establish connection
        // setConnectionRequestTimeout: time to get a connection from the connection pool
        // setSocketTimeout: time to wait for the server to respond
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000)
                .setSocketTimeout(3000).build();
        // not very useful, can create custom implementation of HttpRequestRetryHandler interfaces
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();

        /**
         * client does not know if server closes connection. HttpClient will check if the connection timed out before
         * using this connection. That will add some cost. Add a timed task to close these connections can remove
         * this cost in some degree.
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 关闭失效连接并从连接池中移除
                    connectionManager.closeExpiredConnections();
                    // 关闭30秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
                    connectionManager.closeIdleConnections(20, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 0 , 1000 * 5);
    }

}
