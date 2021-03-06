package github.incodelearning.http;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * A UDP server echos back whatever received from a client.
 */
public class EchoServer extends Thread {
    public static final String ECHO = "echo: ";

    DatagramSocket socket;
    boolean running;
    byte[] buf = new byte[256];

    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;
        int cycle = 0;
        while (running) {
            System.out.println("server running, cycle " + cycle++);
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
//                bug: with line below, client sent: "hello server", received: hello server                 ...
//                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                String response = ECHO + received;
                byte[] responseBytes = response.getBytes();
                DatagramPacket packet2 = new DatagramPacket(responseBytes, responseBytes.length, address, port);
                socket.send(packet2);
                if (received.equals("end")) running = false;
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        System.out.println("server: closing socket");
        socket.close();
    }
}
