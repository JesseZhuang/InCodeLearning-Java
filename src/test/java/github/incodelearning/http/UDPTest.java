package github.incodelearning.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static github.incodelearning.http.EchoServer.ECHO;
import static org.junit.Assert.assertEquals;

public class UDPTest {
    EchoClient client;

    @Before
    public void setup() throws IOException {
        new EchoServer().start();
        client = new EchoClient();
    }

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        String test = "hello server";
        String echo = client.sendEcho(test);
        assertEquals(ECHO + test, echo);
        test = "server is working";
        echo = client.sendEcho(test);
        assertEquals(ECHO + test, echo);
    }

    @After
    public void tearDown() throws IOException {
        client.sendEcho("end");
        client.close();
    }
}
