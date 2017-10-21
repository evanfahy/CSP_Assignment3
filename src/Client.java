// Fig. 18.5: Client.java
// Client that reads and displays information sent from a Server.
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket clientSocket;

    // initialize chatServer and set up GUI
    public Client( String host )
    {
        super( "Client" );

        chatServer = host; // set server to which this clientSocket connects

        Container container = getContentPane();

        // create enterField and register listener
        enterField = new JTextField();
        enterField.setEditable( false );
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

        container.add( enterField, BorderLayout.NORTH );

        // create displayArea
        displayArea = new JTextArea();
        container.add( new JScrollPane( displayArea ),
                BorderLayout.CENTER );

        setSize( 300, 150 );
        setVisible( true );

    } // end Client constructor

    // connect to server and process messages from server
    private void runClient()
    {
        // connect to server, get streams, process connection
        try {
            connectToServer(); // Step 1: Create a Socket to make connection
            getStreams();      // Step 2: Get the input and output streams
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

    } // end method runClient

    // connect to server
    private void connectToServer() throws IOException
    {
        displayMessage( "Attempting connection\n" );

        // create Socket to make connection to server
        clientSocket = new Socket( InetAddress.getByName( chatServer ), 12345 );

        // display connection information
        displayMessage( "Connected to: " +
                clientSocket.getInetAddress().getHostName() );
    }

    // get streams to send and receive data
    private void getStreams() throws IOException
    {
        // set up output stream for objects
        output = new ObjectOutputStream( clientSocket.getOutputStream() );
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream( clientSocket.getInputStream() );

        displayMessage( "\nGot I/O streams\n" );
    }

    // process connection with server
    private void processConnection() throws IOException
    {
        // enable enterField so clientSocket user can send messages
        setTextFieldEditable( true );

        do { // process messages sent from server

            // read message and display it
            try {
                message = ( String ) input.readObject();
                displayMessage( "\n" + message );
            }

            // catch problems reading from server
            catch ( ClassNotFoundException classNotFoundException ) {
                displayMessage( "\nUnknown object type received" );
            }

        } while ( !message.equals( "SERVER>>> TERMINATE" ) );

    } // end method processConnection

    // close streams and socket
    private void closeConnection()
    {
        displayMessage( "\nClosing connection" );
        setTextFieldEditable( false ); // disable enterField

        try {
            output.close();
            input.close();
            clientSocket.close();
        }
        catch( IOException ioException ) {
            ioException.printStackTrace();
        }
    }

    // send message to server
    private void sendData( String message )
    {
        // send object to server
        try {
            output.writeObject( "CLIENT>>> " + message );
            output.flush();
            displayMessage( "\nCLIENT>>> " + message );
        }

        // process problems sending object
        catch ( IOException ioException ) {
            displayArea.append( "\nError writing object" );
        }
    }

    // utility method called from other threads to manipulate
    // displayArea in the event-dispatch thread
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

    // utility method called from other threads to manipulate
    // enterField in the event-dispatch thread
    private void setTextFieldEditable( final boolean editable )
    {
        // display message from GUI thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run()  // sets enterField's editability
                    {
                        enterField.setEditable( editable );
                    }

                }  // end inner class

        ); // end call to SwingUtilities.invokeLater
    }

    public static void main( String args[] )
    {
        Client application;

        if ( args.length == 0 )
            application = new Client( "127.0.0.1" );
        else
            application = new Client( args[ 0 ] );

        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runClient();
    }

} // end class Client
