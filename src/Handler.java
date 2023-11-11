
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
    public void process(Socket clientSocket, ConcurrentHashMap<String, Handler> userMap) throws IOException {

        BufferedReader newClientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter newClientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        newClientWriter.println("Hello");
        try {
            while (true) {
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
