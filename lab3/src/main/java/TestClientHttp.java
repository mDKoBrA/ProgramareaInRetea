import org.apache.http.HttpHost;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestClientHttp
{
    public static void main(String[] args) throws Exception
    {
        ClientHttp client = new ClientHttp("www.facebook.com",443);
        Scanner myInput = new Scanner( System.in );

        for(int i = 0; i != 6;)
        {
            System.out.println("\n\nSelect an option:\n1) Send an get request\n2) Send an option request\n3) Send an head " +
                    "request\n4) Send an post request\n5) Login and print all friends\n6) Exit\n");
            i = myInput.nextInt();
            switch (i)
            {
                case 1:
                    client.get();
                    break;
                case 2:
                    client.option();
                    client.setTarget(new HttpHost("www.ok.ru", 443, "https"));
                    client.option();
                    client.setTarget(new HttpHost("www.facebook.com", 443, "https"));
                    break;
                case 3:
                    client.head();
                    break;
                case 4:
                    Map<String, String> map = new HashMap<>();
                    map.put("name", "Laborator4");
                    map.put("duration", "4");
                    client.postJSON(new URI("http://localhost:8080/ride_tracker_war_exploded/ride"), map);
                    break;
                case 5:
                    client.loginAndPrintFriends();
                case 6:
                    break;
                default:
                    System.out.println("Wrong option!");
            }
        }
    }
}
