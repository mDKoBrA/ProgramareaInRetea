import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHttp
{
    RequestConfig config;
    HttpHost target;
    HttpHost proxy = new HttpHost("ip_proxy", 45785);
    CloseableHttpClient httpClient;

    ClientHttp(String url, int port)
    {
        target = new HttpHost(url, port, "https");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(
                new AuthScope("ip_proxy", 45785),
                new UsernamePasswordCredentials("username_proxy", "password_proxy"));

        httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
    }

    public void get() throws Exception
    {
            HttpGet httpget = new HttpGet("/");
            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);

            CloseableHttpResponse response = httpClient.execute(target, httpget);
            try
            {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
    }

    public void option() throws Exception
    {
        HttpOptions httpOptions = new HttpOptions();
        httpOptions.setConfig(config);

        System.out.println("\nExecuting request " + httpOptions.getRequestLine() + " to " + target + " via " + proxy);

        CloseableHttpResponse response = httpClient.execute(target, httpOptions);
        try
        {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(Arrays.toString(response.getAllHeaders()));
        } finally {
            response.close();
        }
    }

    public void head() throws Exception
    {
        HttpHead httpHead = new HttpHead();
        httpHead.setConfig(config);

        System.out.println("Executing request " + httpHead.getRequestLine() + " to " + target + " via " + proxy);

        CloseableHttpResponse response = httpClient.execute(target, httpHead);
        try
        {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(Arrays.toString(response.getAllHeaders()));
        } finally
        {
            response.close();
        }
    }

    public void postJSON(URI uri, Map<String,String> map) throws IOException, ExecutionException, InterruptedException
    {

        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse:: body)
                .thenAccept(System.out::println).get();
    }

    public void loginAndPrintFriends() throws IOException, InterruptedException
    {
            String cookies =  "my_cookies";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://" + target.getHostName()+ "/sandu.chetroi/friends"))
                    .setHeader("cookie", cookies)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("----------------------------------------");
            System.out.println(response.statusCode());
            System.out.println(response.headers());
            System.out.println(response.body());

            PrintWriter printWriter = new PrintWriter(new FileWriter("C:\\Users\\perep\\Desktop\\get.txt"));
            printWriter.print(response.body());
            printFriends();
    }

    private void printFriends() throws IOException
    {
        List<String> matches = new ArrayList<>();
        Pattern patt = Pattern.compile("\"[A-Za-z ]+\",\"profile_picture\"");

        BufferedReader r = new BufferedReader(new FileReader("C:\\Users\\alexa\\Desktop\\get.txt"));

        String line;
        while ((line = r.readLine()) != null)
        {
            Matcher m = patt.matcher(line);
            while (m.find())
            {
                int start = m.start(0);
                int end = m.end(0);
                matches.add(line.substring(start, end));
            }
        }

        matches = extractNames(matches);
        matches = removeDuplicates(matches);
        matches.remove("Sandu Chetroi");

        System.out.println("\n\nList of my Friends on Facebook:\n" );
        for(String s : matches)
            System.out.println(s);
    }

    public List<String> extractNames(List<String> matches)
    {
        List<String> realMatches = new ArrayList<>();

        for(String string : matches)
            realMatches.add(string.substring(1, string.indexOf("\"", 1)));

        return realMatches;
    }

    public List<String> removeDuplicates(List<String> list)
    {
        List<String> newList = new ArrayList<String>();
        for (String element : list)
        {
            if (!newList.contains(element))
                newList.add(element);
        }
        return newList;
    }

    public void setTarget(HttpHost target)
    {
        this.target = target;
    }
}