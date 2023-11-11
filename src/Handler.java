
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
        public void process(Socket clientSocket) throws IOException {
        Socket originSocket = null;
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientRequest = clientReader.readLine();

            String[] requestParts = clientRequest.split(" ");
            if (requestParts.length != 3) {
                System.out.println("Invalid HTTP request from the client.");
                return;
            }

            // String method = requestParts[0];
            // String url = requestParts[1];
            // String version = requestParts[2];
            //System.out.println(method);
            //System.out.println(url);
            //System.out.println(version);

            // if (!method.equalsIgnoreCase("GET")) {
            //     System.out.println("Unsupported HTTP method: " + method);
            //     return;
            // }

            // String[] urlParts = url.split("/");
            // if (urlParts.length < 3) {
            //     System.out.println("Invalid URL in HTTP request.");
            //     return;
            // }

            //String host = urlParts[2];
			//System.out.println("resource");
			String host = clientRequest.split(" ")[1].split("/")[1];
			// System.out.println(clientRequest.split(" ")[1].split("/")[1]);
			//System.out.println(clientRequest.split(" ")[1].split("/")[2]);
			// System.out.println("Something");
            String resource = clientRequest.split(host)[1];
			// System.out.println("Another");
			System.out.println(host);
			//System.out.println(resource);
			if (resource.equals(" HTTP/1.1")) {
				resource = "/" + resource;
			}
			System.out.println(resource);
			


            if (resource.isEmpty()) {
                resource = "/";
            }

            originSocket = new Socket(host, 80);

            DataOutputStream originOut = new DataOutputStream(originSocket.getOutputStream());
            originOut.writeBytes("GET " + resource + "\r\n");
            originOut.writeBytes("Host: " + host + "\r\n");
            originOut.writeBytes("Connection: close\r\n\r\n");
            originOut.flush();

            BufferedInputStream originIn = new BufferedInputStream(originSocket.getInputStream());
            BufferedOutputStream clientOut = new BufferedOutputStream(clientSocket.getOutputStream());
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = originIn.read(buffer)) != -1) {
                clientOut.write(buffer, 0, bytesRead);
            }
            clientOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (originSocket != null) {
                    originSocket.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}