
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a single-threaded TCP server. It is able to interact
 * with only one client at the time. If a client tries to connect when
 * the server is busy with another one, it will have to wait.
 *
 * @author Olivier Liechti
 * @modified Lionel Burgbacher
 */
public class TcpServer {

    final static Logger LOG = Logger.getLogger(TcpServer.class.getName());

    int port;

    /**
     * Constructor
     * @param port the port to listen on
     */
    public TcpServer(int port) {
        this.port = port;
    }

    /**
     * This method initiates the process. The server creates a socket and binds
     * it to the previously specified port. It then waits for clients in a infinite
     * loop. When a client arrives, the server will read its input line by line
     * and send back the data converted to uppercase. This will continue until
     * the client sends the "BYE" command.
     */
    public void serveClients() {
        ServerSocket serverSocket;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        while (true) {
            try {

                LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", port);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
                out = new PrintWriter(clientSocket.getOutputStream());
                String line;
                boolean shouldRun = true;

                String errorMessage = "Wrong syntaxe, try again";
                String  needSomething = "Do you need something else ? \n" +
                        "If you want to see the command again use CMD";
                String command = "\nYou can ask for addition (X + Y)\n" +
                        "You can ask for subtraction (X - Y)\n" +
                        "You can ask for multiplication (X * Y)\n" +
                        "You can ask for division (X / Y)\n\n" +
                        "You can leave with BYE";

                out.println("\nWelcome to Simple Calculator.\n" + command);

                out.flush();
                LOG.info("Reading until client sends BYE or closes the connection...");
                while ( (shouldRun) && (line = in.readLine()) != null ) {
                    if (line.equalsIgnoreCase(Protocol.CMD_BYE)) {
                        shouldRun = false;
                    }
                    else if (line.contains(Protocol.CMD_CMD) || line.contains(Protocol.CMD_CMD.toLowerCase())){

                        out.println(command);

                    }
                    else if (line.contains(Protocol.CMD_ADD)){

                        double[] numbers;
                        if(!numberElementIsCorrect(line)){

                            out.println(errorMessage);
                        }
                        else {

                            numbers = verify(line);
                            boolean isOk = true;

                            if(numbers[2] == 1){

                                isOk = false;
                                out.println(errorMessage);
                            }

                            if(isOk){
                                out.println(numbers[0] + " + " + numbers[1] + " = " + (numbers[0] + numbers[1]));
                                out.println(needSomething);
                            }
                        }

                    }
                    else if (line.contains(Protocol.CMD_MULT)){

                        double[] numbers;
                        if(!numberElementIsCorrect(line)){

                            out.println(errorMessage);
                        }
                        else {

                            numbers = verify(line);
                            boolean isOk = true;

                            if(numbers[2] == 1){

                                isOk = false;
                                out.println(errorMessage);
                            }

                            if(isOk){
                                out.println(numbers[0] + " * " + numbers[1] + " = " + (numbers[0] * numbers[1]));
                                out.println(needSomething);
                            }
                        }

                    }
                    else if (line.contains(Protocol.CMD_DIV)){

                        double[] numbers;
                        if(!numberElementIsCorrect(line)){

                            out.println(errorMessage);
                        }
                        else {

                            numbers = verify(line);
                            boolean isOk = true;

                            if(numbers[2] == 1){

                                isOk = false;
                                out.println(errorMessage);
                            }

                            if(isOk){
                                out.println(numbers[0] + " / " + numbers[1] + " = " + (numbers[0] / numbers[1]));
                                out.println(needSomething);
                            }
                        }

                    }
                    else if (line.contains(Protocol.CMD_SUB)){

                        double[] numbers;
                        if(!numberElementIsCorrect(line)){

                            out.println(errorMessage);
                        }
                        else {

                            numbers = verify(line);
                            boolean isOk = true;

                            if(numbers[2] == 1){

                                isOk = false;
                                out.println(errorMessage);
                            }

                            if(isOk){
                                out.println(numbers[0] + " - " + numbers[1] + " = " + (numbers[0] - numbers[1]));
                                out.println(needSomething);
                            }
                        }

                    }
                    else{

                        out.println("Warning! You have to use a space as separator and write the action you want");
                    }
                    out.flush();
                }

                LOG.info("Cleaning up resources...");
                clientSocket.close();
                in.close();
                out.close();

            } catch (IOException ex) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public double[] verify(String line){

        String[] str = line.split(" ");
        double[] number = new double[3];
        number[2] = 0;

        try {
            number[0] = Double.parseDouble(str[0]);
            number[1] = Double.parseDouble(str[2]);
        }catch (NumberFormatException e) {

            number[2] = 1;
        }

        return number;
    }

    public boolean numberElementIsCorrect(String line){

        String[] str = line.split(" ");
        return str.length == 3;
    }

}