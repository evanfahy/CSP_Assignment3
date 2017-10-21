import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient extends NetworkClient {

    /** Pass host and port to parent constructor. */

    public MyClient(String host, int port) {
        super(host, port);
    }

    /** Simple client that sends a single line
     *  ("Generic Network Client") to the server,
     *  reads one line of response, prints it, then exits.
     */
    @Override
    protected void handleConnection(Socket client) throws IOException {
        PrintWriter out = SocketUtils.getWriter(client);
        BufferedReader in = SocketUtils.getReader(client);
        out.println("Generic Network Client");
        System.out.printf("Generic Network Client:%n" +
                        "Connected to '%s' and got '%s' in response.%n",
                getHost(), in.readLine());
    }
}
