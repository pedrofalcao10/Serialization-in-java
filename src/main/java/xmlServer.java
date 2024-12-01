import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class xmlServer {
    public static void main(String[] args) {
        final int PORT = 65432;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("New client connected: " + connectionSocket.getInetAddress());

                // Handle client in a new thread
                new Thread(() -> handleClient(connectionSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public static void handleClient(Socket connectionSocket) {
        try (
                CountingInputStream countingInputStream = new CountingInputStream(connectionSocket.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(countingInputStream));
        ) {
            //Read XML data from the client
            StringBuilder xmlDataBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlDataBuilder.append(line).append("\n");
            }
            String xmlData = xmlDataBuilder.toString();
            System.out.println("Received XML data:\n" + xmlData);

            //Measure bytes received
            long receivedBytes = countingInputStream.getBytesRead();
            System.out.println("Total bytes received from client: " + receivedBytes);

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                connectionSocket.close();
            }
            catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}