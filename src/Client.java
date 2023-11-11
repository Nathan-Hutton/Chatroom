import java.io.*;
import java.net.*;

public class Client
{

    public static final int PORT = 5040;

    public static void main(String[] args) {
        // The actual process
        try (
            Socket clientSocket = new Socket("146.86.107.164", PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            String test = fromServer.readLine();
            System.out.println(test);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
