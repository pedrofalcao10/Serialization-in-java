
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class customServer {
    public static void main(String[] args) {
        final int PORT = 55555;

        try (ServerSocket listenSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = listenSocket.accept();
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
                DataInputStream input = new DataInputStream(countingInputStream)
        ) {
            while (true) {
                // Read and log each field
                String name = input.readUTF();
                String localization = input.readUTF();
                int age = input.readInt();
                int numID = input.readInt();

                System.out.println("Received Person:");
                System.out.println("  Name (String): " + name);
                System.out.println("  Localization (String): " + localization);
                System.out.println("  Age (int): " + age);
                System.out.println("  NumID (int): " + numID);
                System.out.println("-----------------------");
            }
        }
        catch (EOFException e) {
            System.out.println("Client disconnected.");
        }
        catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
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

