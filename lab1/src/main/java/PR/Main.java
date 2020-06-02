package PR;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;


public class Main {

    private static final String uniteHost = "unite.md";
    private static final String utmHost = "utm.md";

    public static void main(String[] args) throws IOException {
        HttpSocket uniteSocket = new HttpSocket(uniteHost, 80);
        InetAddress inetAddress = InetAddress.getByName(utmHost);
        SslSocket utmSocket = new SslSocket(inetAddress,443);

        int choice;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("[0] Quit");
            System.out.println("[1] www.unite.md");
            System.out.println("[2] www.utm.md");
            System.out.print("Enter: ");
            choice = in.nextInt();

            switch (choice) {
                case 1:
                    try {
                        uniteSocket.httpSocketConnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        utmSocket.sslSocketConnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        while (choice != 0);
    }
}