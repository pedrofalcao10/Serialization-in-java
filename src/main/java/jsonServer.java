import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class jsonServer {
    public static void main(String[] args) {
        final int PORT = 55555;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("New client connected: " + connectionSocket.getInetAddress());

                //Handle client in a new thread
                new Thread(() -> handleClient(connectionSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public static void handleClient(Socket connectionSocket) {
        try {
            DataInputStream dis = new DataInputStream(connectionSocket.getInputStream());

            // Receive the length of the JSON data
            int dataLength = dis.readInt();
            System.out.println("Received JSON size: " + dataLength + " bytes");

            // Read the JSON data
            byte[] dataBuffer = new byte[dataLength];
            dis.readFully(dataBuffer);
            String jsonData = new String(dataBuffer);
            System.out.println("Received JSON data: " + jsonData);
        }
        catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                connectionSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}