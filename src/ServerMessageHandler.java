import java.io.*;
// I got this idea from stack overflow: https://stackoverflow.com/questions/22301892/print-to-console-while-waiting-for-input-in-java

class RunnableMessageHandler implements Runnable {
    private BufferedReader fromServer = null;

    public RunnableMessageHandler(BufferedReader fromServer) {
        this.fromServer = fromServer;
    }

    @Override
    public void run() {
        String serverOutput;
        try {
            while (true) {
                serverOutput = fromServer.readLine();
                if (serverOutput == null)
                    break;
                System.out.println(serverOutput);
           }
        }
        catch (IOException e) {
            return;
        }
    }
    // We will have to call static methods from the Client class to parse what the server sends us
}
