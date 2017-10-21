import java.net.*;
import java.io.*;

/** A starting point for network servers. Instantiate
 *  it and call "listen" to start it up. You'll need to
 *  override handleConnection, but in many cases listen can
 *  remain unchanged. NetworkServer uses SocketUtil to simplify
 *  the creation of the PrintWriter and BufferedReader.
 *  <p>
 *  From <a href="http://courses.coreservlets.com/Course-Materials/">the
 *  coreservlets.com tutorials on JSF 2, PrimeFaces, Ajax, JavaScript, jQuery, GWT, Android,
 *  Spring, Hibernate, JPA, RESTful Web Services, Hadoop, Spring MVC,
 *  servlets, JSP, Java 8 lambdas and streams (for those that know Java already),
 *  and Java 8 programming (for those new to Java)</a>.
 */

public abstract class NetworkServer {


    /** Build a server on specified port. It will continue to
     *  accept connections, passing each to handleConnection until
     *  the server is killed (e.g., Control-C in the startup window)
     *  or System.exit() from handleConnection or elsewhere
     *  in the Java code).
     */
    private int port;

    public NetworkServer(int port) {
        this.port = port;
    }

    /** Monitor a port for connections. Each time one is
     *  established, pass resulting Socket to handleConnection.
     */

    public void listen() {
        try(ServerSocket listener = new ServerSocket(port)) {
            Socket socket;
            while(true) {  // Run until killed
                socket = listener.accept();
                handleConnection(socket);
            }
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
            ioe.printStackTrace();
        }
    }

    /** This is the method that provides the behavior to the
     *  server, since it determines what is done with the
     *  resulting socket. <b>Override this method in servers
     *  you write.</b>
     */

    protected abstract void handleConnection(Socket socket)
            throws IOException;

    /** Gets port on which server is listening. */

    public int getPort() {
        return(port);
    }
}