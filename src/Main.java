import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        System.out.println("Console test");
        int port = 12345;
        try {
            port = Integer.parseInt(args[0]);
            System.out.println(args[0]);
        } catch(NumberFormatException|ArrayIndexOutOfBoundsException e) {}

        MyServer myServer = new MyServer(port);
        myServer.listen();
        //myServer.handleConnection(new Socket);

        String host = "127.0.0.1";
        int port1 = 21;
        if (args.length > 1) {
            host = args[1];
        }
        if (args.length > 2) {
            port = Integer.parseInt(args[2]);
        }
        MyClient myClient = new MyClient(host, port);
        myClient.connect();
    }
}
