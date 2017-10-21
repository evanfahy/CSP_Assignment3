import java.net.*;
import java.io.*;

/** Simple test of the NetworkServer parent class.
 *  <p>
 *  From <a href="http://courses.coreservlets.com/Course-Materials/">the
 *  coreservlets.com tutorials on JSF 2, PrimeFaces, Ajax, JavaScript, jQuery, GWT, Android,
 *  Spring, Hibernate, JPA, RESTful Web Services, Hadoop, Spring MVC,
 *  servlets, JSP, Java 8 lambdas and streams (for those that know Java already),
 *  and Java 8 programming (for those new to Java)</a>.
 */

public class NetworkServerTest extends NetworkServer {
    public NetworkServerTest(int port) {
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
        System.out.printf
                ("Generic Server: got connection from %s%n" +
                                "with first line '%s'.%n",
                        socket.getInetAddress().getHostName(),
                        in.readLine());
        out.println("Generic Server");
        socket.close();
    }

    public static void main(String[] args) {
        int port = 1234;
        try {
            port = Integer.parseInt(args[0]);
        } catch(NumberFormatException|ArrayIndexOutOfBoundsException e) {}
        NetworkServerTest tester =
                new NetworkServerTest(port);
        tester.listen();
    }
}