
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
import java.util.concurrent.TimeUnit;

public class Handler {
    private Socket clientSocket = null;
    private BufferedReader fromClient = null;
    private PrintWriter toClient = null;
    private String username = null;
    private ConcurrentHashMap<String, PrintWriter> userMap;

    public void process(Socket clientSocket, ConcurrentHashMap<String, PrintWriter> userMap) throws IOException {
        try  {
            this.clientSocket = clientSocket;
            this.userMap = userMap;
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            String clientCommand;

            while (true) {
                clientCommand = fromClient.readLine();
                System.out.println(clientCommand);

                // Likely means that the user left without an exit command
                if (clientCommand == null)
                    break;

                int returnCode = processCommand(clientCommand);

                // The user left
                if (returnCode == -1) {
                    break;
                }
            }
        } catch (IOException e) {
            //System.out.println(e);
            //e.printStackTrace();
        } finally {
            if (clientSocket != null)
                clientSocket.close();
            if (fromClient != null)
                fromClient.close();
            if (toClient != null)
                toClient.close();
            if (userMap.keySet().contains(username))
                userMap.remove(username);
            if (username != null) {
                for (PrintWriter userWriter : userMap.values())
                    userWriter.println(username + " has left the server");
            }
        }
    }

    public int processCommand(String command) throws IOException {
        String commandHeader = command.split("<")[0];

        if (commandHeader.equals("user"))
            return processNewUserRequest(command);

        if (commandHeader.equals("exit"))
            return processExitRequest(command);

        return 1;
    }

    public int processNewUserRequest(String command) {
        String[] commandParts = command.split("<");

        // Means that we have an empty command body
        if (commandParts[1].equals(">")) {
            getClientPrintWriter().println(3);
            return 1;
        }

        String commandBody = commandParts[1].split(">")[0];

        // Username taken
        if (userMap.keySet().contains(commandBody) || commandBody.equals("server")) {
            getClientPrintWriter().println(1);
            return 1;
        }
        
        // Reserved character used
        if (commandBody.contains("<") || commandBody.contains(">") || commandBody.contains(",")) {
            getClientPrintWriter().println(2);
            return 1;
        }

        // Length of username is invalid
        if (commandBody.length() < 1 || commandBody.length() > 20) {
            getClientPrintWriter().println(3);
            return 1;
        }

        // Success
        userMap.put(commandBody, getClientPrintWriter());
        username = commandBody;

        // We'll need to make this a broadcast message at some point
        getClientPrintWriter().println(4);
        for (PrintWriter toClient : userMap.values())
            toClient.println(commandBody + " has joined the server");            

        return 1;
    }

    public int processExitRequest(String command) {
        String[] commandParts = command.split("<");
        String commandBody = commandParts[1].split(">")[0];

        userMap.remove(commandBody);
        try {
            toClient.close();
            fromClient.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return -1;
    }

    public PrintWriter getClientPrintWriter() {
        return this.toClient;
    }
}
