package github.incodelearning.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientWithPool {

    public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
        pool.setDefaultMaxPerRoute(1);
        pool.setMaxTotal(1);
        final CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(pool).build();

        final String url = "http://localhost:8000/test";
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    HttpGet httpGet = new HttpGet(url);
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    EntityUtils.consume(entity);

                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(100000);
    }

}
