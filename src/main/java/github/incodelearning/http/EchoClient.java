package github.incodelearning.http;

import java.io.IOException;
import java.net.*;

/**
 * A client that can talk to a {@link EchoServer} through UDP.
 */
public class EchoClient {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;

    public EchoClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        buf = new byte[256];
    }

    public String sendEcho(String msg) {
        DatagramPacket packet = null;
        try {
            byte[] msgBytes = msg.getBytes();
            packet = new DatagramPacket(msgBytes, msgBytes.length, address, 4445);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        System.out.println("client: closing socket");
        socket.close();
    }
}
