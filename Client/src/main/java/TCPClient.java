import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {

    final static Logger LOG = Logger.getLogger(TCPClient.class.getName());


    int port;
    InetAddress address;
    Socket socket;
    final BufferedReader in;
    final PrintWriter out;
    boolean connectionOpened;
    volatile  boolean connectionLostUnexpectedly = false;

    public TCPClient(InetAddress destination, int destinationPort) throws IOException {
        this.port = destinationPort;
        this.address = destination;

        socket = new Socket(address, port);

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException ioe) {
            //closing resources before propagating the error
            socket.close();
            throw ioe;
        }

        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException ioe) {
            //closing resources before propagating the error
            socket.close();
            in.close();
            throw ioe;
        }

        connectionOpened = true;
    }

    public void closeConnection() throws IOException {
        if (!connectionOpened) return;

        try {
            socket.close();
        } catch (IOException ioeSocket) {
            LOG.log(Level.SEVERE, "Failed to close the socket");
            throw ioeSocket;
        }

        try {
            in.close();
        } catch (IOException ioeIn) {
            LOG.log(Level.SEVERE, "Failed to close the reader listening on socket inputStream");
            throw ioeIn;
        }

        out.close();

        connectionOpened = false;
    }

    public void commandLineCommunication() throws IOException, InterruptedException {

        //Defines the thread that will display messages coming from the server
        Thread displayServerMessages = new Thread(new Runnable() {
            public void run() {

                BufferedReader userInput = new BufferedReader(
                        new InputStreamReader(System.in));
                String userCommand;

                try {

                    do {
                        userCommand = userInput.readLine();
                        out.println(userCommand);
                        out.flush();

                    } while (!userCommand.toUpperCase().equals(Protocol.CMD_BYE));

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    return;
                }
            }
        }
        );

        //Defines the thread that will send user input to the server
        Thread sendUserInput = new Thread(new Runnable() {
            public void run() {
                String serverMessageLine;
                try {
                    while ((serverMessageLine = in.readLine()) != null) {
                        System.out.println(serverMessageLine);
                    }
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Connection with the server lost unexpectedly");
                    connectionLostUnexpectedly = true;
                    return;
                }
            }
        });

        //start two threads:
        //one display the server messages
        //the other send user input to the server
        displayServerMessages.start();
        sendUserInput.start();

        sendUserInput.join();
        if(connectionLostUnexpectedly){
            throw new InterruptedException();
        }
        displayServerMessages.join();

        closeConnection();

        return;
    }


}
