import java.io.*;
import java.net.*;
import java.util.Scanner; 
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Client {

    public static final int PORT = 5040;
    private String username;

    public static void main(String[] args) {
        Client client = new Client();
        String userInput;
        String serverOutput;
        String command;
        try (
            Socket clientSocket = new Socket("localhost", PORT);
            PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner scanner = new Scanner(System.in); 
        ) {
            // Setup username
            System.out.print("Enter your username: ");
            userInput = scanner.nextLine();
            client.setUsername(userInput);
            toServer.println("user<" + userInput + ">"); 

            while (clientSocket.isConnected()) {
                serverOutput = fromServer.readLine();
                System.out.println(serverOutput);

                // This means we've left the server
                if (serverOutput == null) {
                    break;
                }

                // TODO: Add colored text
                // This means we just got a code back from the server
                if (serverOutput.length() == 1)
                    handleServerCode(Integer.parseInt(serverOutput), toServer, scanner, client);
            }

            System.out.println("You've left the server");
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    // Handle a code sent from the server (1 through 9)
    public static void handleServerCode(int code, PrintWriter toServer, Scanner scanner, Client client) {
        String userInput;
        String command;

        switch (code) {
            case 1:
                client.setUsername("");
                System.out.println("Username taken");
                System.out.print("Enter a different username: ");
                userInput = scanner.nextLine();
                client.setUsername(userInput);
                command = "user<" + userInput + ">";
                toServer.println(command);
                return;
            case 2:
                client.setUsername("");
                System.out.println("Protected character used");
                System.out.print("Enter a different username: ");
                userInput = scanner.nextLine();
                client.setUsername(userInput);
                command = "user<" + userInput + ">";
                toServer.println(command);
                return;
            case 3:
                client.setUsername("");
                System.out.println("Length of the username is invalid");
                System.out.print("Enter a different username: ");
                userInput = scanner.nextLine();
                client.setUsername(userInput);
                command = "user<" + userInput + ">";
                toServer.println(command);
                return;
            case 4:
                // TODO: make this text green and clean this up
                String BRIGHT_GREEN = "\u001B[92m";
                String BOLD = "\u001B[1m";
                String RESET = "\u001B[0m";
                System.out.println(BRIGHT_GREEN + BOLD + "Success" + RESET);
                //System.out.println("You can send broadcast messages with this format: '(broadcast)Hey everyone'");
                //System.out.println("You can send private messages with this format: '(private recipient_username)Hey everyone'");
                //System.out.println("Use 'ls' to get a list of users");
                //System.out.println("Use 'exit' to leave");
                System.out.print("> ");
                userInput = scanner.nextLine();
                command = parseUserInput(userInput, scanner, client);
                toServer.println(command);
                return;
            case 5:
                System.out.println("Invalid message length");
                System.out.print("> ");
                userInput = scanner.nextLine();
                command = parseUserInput(userInput, scanner, client);
                toServer.println(command);
                return;
            case 6:
                System.out.println("Reserved character used");
                System.out.print("> ");
                userInput = scanner.nextLine();
                command = parseUserInput(userInput, scanner, client);
                toServer.println(command);
                return;
            case 7:
                return;
            case 8:
                return;
            case 9:
                System.out.println("User not found");
                System.out.print("> ");
                userInput = scanner.nextLine();
                command = parseUserInput(userInput, scanner, client);
                toServer.println(command);
                return;
        }
    }

    public static String parseUserInput(String userInput, Scanner scanner, Client client) {
        userInput = userInput.stripTrailing();
        if (userInput.equals("exit"))
            return "exit<" + client.getUsername() + ">";
        if (userInput.equals("ls"))
            return "<ls>";

        // Check if there aren't parenthesis
        if (userInput.charAt(0) != '(' || !userInput.contains(")")) {
            System.out.println("Invalid request, message must contain parenthesis");
            System.out.print("> ");
            userInput = scanner.nextLine();
            return parseUserInput(userInput, scanner, client);
        }

        // User probably just has 2 parenthesis with nothing in them
        if (userInput.length() <= 2) {
            System.out.println("Invalid command");
            System.out.print("> ");
            userInput = scanner.nextLine();
            return parseUserInput(userInput, scanner, client);
        }

        // Check that what's in parenthesis is 'broadcast' or 'private'
        // If it's private, check that there's room for a username with substring
        String betweenParenthesis = userInput.substring(1, userInput.indexOf(")"));
        if (!(betweenParenthesis.equals("broadcast") || (betweenParenthesis.substring(0, 7).equals("private") && betweenParenthesis.length() > 7))) {
            System.out.println("Invalid: '" + betweenParenthesis + "'");
            System.out.print("> ");
            userInput = scanner.nextLine();
            return parseUserInput(userInput, scanner, client);
        }

        // Handle private messages
        if (betweenParenthesis.substring(0, 7).equals("private")) {
            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = time.format(formatter);
            String recipient = betweenParenthesis.substring(7);
            String message = userInput.split("\\)", 2)[1];

            String command = "<private>("+client.getUsername()+","+recipient+","+formattedTime+","+message+")";
            return command;
        }

        // Handle broadcast messages
        if (betweenParenthesis.equals("broadcast")) {
            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = time.format(formatter);
            String message = userInput.split("\\)", 2)[1];

            String command = "<broadcast>("+client.getUsername()+","+formattedTime+","+message+")";
            return command;
        }

        return "";


    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
