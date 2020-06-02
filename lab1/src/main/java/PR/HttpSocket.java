package PR;
import java.io.*;
import java.net.Socket;


public class HttpSocket {
    private static final String UNITE_IMAGE_DESTINATION_FOLDER = "D:\\Downloads\\Programarea-in-retea-master\\Programarea-in-retea-master\\lab1\\src\\main\\java\\PR\\images\\utm";
    private final String host;
    private final int port;

    public HttpSocket(String host,int port) {
        this.host = host;
        this.port = port;
    }

    public void httpSocketConnect() throws Exception {

        InputStream result;

        try (var socket = new Socket(host, 80)) {

            try (var wtr = new PrintWriter(socket.getOutputStream())) {

                // create GET request with the following headers
                wtr.print("GET / HTTP/1.1\r\n");
                wtr.print("Host: " + host + "\r\n");
                wtr.print("Accept:\r\n\r\n");
                wtr.flush();
                socket.shutdownOutput();

                // takes input from the client socket
                result = socket.getInputStream();


                // reads message from client until "outStr" is null
                String outStr;
                try (var bufRead = new BufferedReader(new InputStreamReader(result))) {
                    while ((outStr = bufRead.readLine()) != null) {
                        System.out.println(outStr);
                    }
                    if(socket.isClosed())
                    socket.shutdownInput();

                    String url = "http://" + host;

                    //4 threads for downloading the images
                    Image image = new Image(UNITE_IMAGE_DESTINATION_FOLDER,url);
                    ThreadImage t1 = new ThreadImage(image);
                    ThreadImage t2 = new ThreadImage(image);
                    ThreadImage t3 = new ThreadImage(image);
                    ThreadImage t4 = new ThreadImage(image);

                    t1.start();
                    t2.start();
                    t3.start();
                    t4.start();

                    try{
                        t1.join();
                        t2.join();
                        t3.join();
                        t4.join();
                    }
                    catch (InterruptedException e){
                        System.out.println("Interrupted");
                    }
                }
            }
        }
    }
}
