package pr;


public class Main {
    public static void main(String[] args) {

        String host = "pop.gmail.com";
        String mailStoreType = "pop3";
        String username = "fistudent";
        String password = "fi171student";
        EmailUtil.check(host, mailStoreType, username, password);
        System.out.println("Sending email...");
        EmailUtil.send(username, password, "asycs97@gmail.com", "Programarea in retea", "Email trimis");

    }
}
