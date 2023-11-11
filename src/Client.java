import java.io.*;
import java.net.*;

public class DNSClient
{

    public static final int PORT = 6052;

    public static void main(String[] args) {
        // Check if the user gave enough arguments
        if (args.length != 2) {
            System.out.println("Not enough arguments provided");
            System.exit(0);
        }

        // The actual process
        try (
            Socket clientSocket = new Socket(args[0], PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            out.println(args[1]);
            String ip = fromServer.readLine();
            System.out.println(ip);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
