
/**
 * Handler class containing the logic for echoing results back
 * to the client. 
 *
 * This conforms to RFC 862 for echo servers.
 *
 * @author Greg Gagne 
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Handler {
    private BufferedReader fromClient = null;
    private PrintWriter toClient = null;

    public void process(Socket clientSocket, ConcurrentHashMap<String, PrintWriter> userMap) throws IOException {

        //newClientWriter.println("Hello");
        try  {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            while (true) {
                String clientCommand = fromClient.readLine();
                processCommand(clientCommand, userMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
            fromClient.close();
            toClient.close();
        }
    }

    public void processCommand(String command, ConcurrentHashMap<String, PrintWriter> userMap) {
        String[] commandParts = command.split("<");
        String commandHeader = commandParts[0];
        String commandBody = commandParts[1].substring(0, commandParts[1].length() - 1);

        // Request for making a new user
        if (commandHeader.equals("user")) {
            if (userMap.keySet().contains(commandBody)) {
                getClientPrintWriter().println(1);
                return;
            }

            userMap.put(commandBody, getClientPrintWriter());

            // We'll need to make this a broadcast message at some point
            for (PrintWriter toClient : userMap.values())
                toClient.println(commandBody + " has joined the server");            

            getClientPrintWriter().println(4);
        }
    }

    public PrintWriter getClientPrintWriter() {
        return this.toClient;
    }
}
