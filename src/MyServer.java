import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer extends NetworkServer {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket serverSocket;
    private Socket serverConnection;
    private int counter = 1;

    public MyServer (int port) {
        super(port);
    }

    /** This handleConnection simply reports the host that made
     *  the connection, shows the first line the client sent,
     *  and sends a single line in response.
     */
    @Override
    protected void handleConnection(Socket socket)
            throws IOException{
        PrintWriter out = SocketUtils.getWriter(socket);
        BufferedReader in = SocketUtils.getReader(socket);
        System.out.printf("Generic Server: got connection from %s%n"
                        + "with first line '%s'.%n", socket.getInetAddress().getHostName(), in.readLine());
        out.println("Generic Server");
        socket.close();
    }
}
