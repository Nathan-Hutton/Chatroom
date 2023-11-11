import java.io.*;
import java.net.*;
import java.util.Scanner; // Import Scanner

public class Client {

    public static final int PORT = 5040;

    public static void main(String[] args) {
        try (
            Socket clientSocket = new Socket("localhost", PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in); 
        ) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            out.println(username); 

            String test = fromServer.readLine();
            System.out.println(test);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
