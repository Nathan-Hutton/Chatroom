/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Connection implements Runnable
{
	private Socket	client;
	private static Handler handler = new Handler();
    private static ConcurrentHashMap<String, PrintWriter> userMap;
	
	public Connection(Socket clientSocket, ConcurrentHashMap<String, PrintWriter> userMap) {
        this.client = clientSocket;
        this.userMap = userMap;
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		try {
			handler.process(client, userMap);
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
	}
}

