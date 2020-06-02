package PR;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


public class SslSocket {
    private static final String UTM_IMAGE_DESTINATION_FOLDER = "D:\\Semestrul VI\\Programarea in retea\\Lab1\\src\\com\\PR\\images\\utm";
    private SSLSocket socket;

    public SslSocket(InetAddress address, int port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) factory.createSocket(address, port);
        socket.startHandshake();
    }

    public void sslSocketConnect() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        //create get request with following headers
        out.println("GET / HTTP/1.1");
        out.println("Host: " + socket.getInetAddress().getHostName());
        out.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        out.println("Accept-Language: en-US,en;q=0.9,ro;q=0.8,ru;q=0.7");
        out.println("Connection: keep-alive");
        out.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
        out.println("");
        out.flush();

        String line;

        // reads message from client until "line" is null
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("\n\n");

        String url = "http://" + socket.getInetAddress().getHostName();


        //4 threads for downloading images
        Image image = new Image(UTM_IMAGE_DESTINATION_FOLDER, url);
        ThreadImage t1 = new ThreadImage(image);
        ThreadImage t2 = new ThreadImage(image);
        ThreadImage t3 = new ThreadImage(image);
        ThreadImage t4 = new ThreadImage(image);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        //close input,output,socket.
        in.close();
        out.close();
        socket.close();
    }
}
