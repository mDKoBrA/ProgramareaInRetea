import java.io.IOException;
import java.net.*;


public class UdpUnicastServer implements Runnable {

    private final int clientPort;

    public UdpUnicastServer(int clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public void run() {

        try (DatagramSocket serverSocket = new DatagramSocket(50000)) {
            for (int i = 0; i < 3; i++) {
                String message = "Message number " + i;
                DatagramPacket datagramPacket = new DatagramPacket(
                        message.getBytes(),
                        message.length(),
                        InetAddress.getLocalHost(),
                        clientPort
                );
                serverSocket.send(datagramPacket);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
