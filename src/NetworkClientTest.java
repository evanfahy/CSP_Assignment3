import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

/** Make simple connection to the host and port specified.
 *  <p>
 *  From <a href="http://courses.coreservlets.com/Course-Materials/">the
 *  coreservlets.com tutorials on JSF 2, PrimeFaces, Ajax, JavaScript, jQuery, GWT, Android,
 *  Spring, Hibernate, JPA, RESTful Web Services, Hadoop, Spring MVC,
 *  servlets, JSP, Java 8 lambdas and streams (for those that know Java already),
 *  and Java 8 programming (for those new to Java)</a>.
 */

public class NetworkClientTest extends NetworkClient {
    /** Pass host and port to parent constructor. */

    private JFrame frame;
    private JTextField enterField;
    private JTextArea displayArea;
    private String message = "Connected to Client";
    //private String chatServer;
    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;

    public int count = 1;


    public NetworkClientTest(String host, int port) {
        super(host, port);

        this.frame = new JFrame("Client " + count);
        Container container;
        container = frame.getContentPane();

        // create enterField and register listener
        this.enterField = new JTextField();
        enterField.addActionListener(
                new ActionListener() {

                    // send message to server
                    public void actionPerformed( ActionEvent event )
                    {
                        sendData( event.getActionCommand() );
                        enterField.setText( "" );
                    }
                }
        );
        this.enterField.setEditable( false );
        container.add( this.enterField, BorderLayout.NORTH );

        // create displayArea
        this.displayArea = new JTextArea();
        displayArea.setEditable(false);
        container.add( new JScrollPane( this.displayArea ), BorderLayout.CENTER );
        //this.displayArea.setText("Client");
        this.frame.setSize( 300, 150 );
        this.frame.setVisible( true );
    }

    /** Simple client that sends a single line
     *  ("Generic Network Client") to the server,
     *  reads one line of response, prints it, then exits.
     */
    @Override
    protected void handleConnection(Socket clientSocket) throws IOException {

        this.clientSocket = clientSocket;

        out = SocketUtils.getWriter(clientSocket);
        in = SocketUtils.getReader(clientSocket);

        out.println("Hello from the Client");
        System.out.printf("Network Client:%n" +
                        "Connected to '%s' and got '%s' in response.%n", getHost(), in.readLine());
        out.flush();
        count ++;

        try {
            //connectToServer(); // Step 1: Create a Socket to make connection
            //getStreams();      // Step 2: Get the input and output streams
            processConnection(); // Step 3: Process connection
        }

        // server closed connection
        catch ( EOFException eofException ) {
            System.err.println( "Client terminated connection" );
        }

        // process problems communicating with server
        catch ( IOException ioException ) {
            ioException.printStackTrace();
        }

        finally {
            closeConnection(); // Step 4: Close connection
        }
    }

    private void processConnection() throws IOException
    {
        // enable enterField so client user can send messages
        enterField.setEditable( true );

        do { // process messages sent from server

            // read message and display it
            try {
                message = ( String ) in.readLine();
                displayMessage("\n" + message  );
            }

            // catch problems reading from server
            catch ( Exception classNotFoundException ) {
                displayMessage( "\nUnknown object type received" );
            }

        } while ( !message.equals( "SERVER>>> TERMINATE" ) );

    } // end method processConnection

    // send message to server
    private void sendData( String message )
    {
        // send object to server
        try {

            out.println( "CLIENT>>> " + message );
            //out.flush();
            displayMessage( "\nCLIENT>>> " + message );
        }

        // process problems sending object
        catch ( Exception ioException ) {
            displayArea.append( "\nError writing object" );
        }
    }

    private void displayMessage( final String messageToDisplay )
    {
        // display message from GUI thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run() // updates displayArea
                    {
                        displayArea.append( messageToDisplay );
                        displayArea.setCaretPosition(
                                displayArea.getText().length() );
                    }

                }  // end inner class

        ); // end call to SwingUtilities.invokeLater
    }

    // close streams and socket
    private void closeConnection()
    {
        displayMessage( "\nClosing connection" );
        enterField.setEditable( false ); // disable enterField

        try {
            out.close();
            in.close();
            clientSocket.close();
        }
        catch( IOException ioException ) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 1234;

        NetworkClientTest tester = new NetworkClientTest(host, port);
        tester.connect();
    }
}