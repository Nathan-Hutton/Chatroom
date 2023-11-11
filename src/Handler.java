
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

public class Handler {
    BufferedReader fromClient = null;
    PrintWriter out = null;
    public void process(Socket clientSocket) throws IOException {
        try {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
