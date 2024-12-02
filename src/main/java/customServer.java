
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
                ObjectInputStream input = new ObjectInputStream(countingInputStream)
        ) {
            System.out.println("Receiving Person objects...");

            while (true) {
                try {
                    System.out.println("Received Person:");

                    // Receive and print fields one by one
                    String name = (String) input.readObject();
                    System.out.println("  Name (String): " + name);

                    String localization = (String) input.readObject();
                    System.out.println("  Localization (String): " + localization);

                    int age = (int) input.readObject();
                    System.out.println("  Age (int32): " + age);

                    String numID = (String) input.readObject();
                    System.out.println("  numID (String): " + numID);

                    System.out.println("----------------------------------------------");
                }
                catch (EOFException e) {
                    System.out.println("Finished receiving all Person objects.");
                    break;
                }
            }

            // Measure how many bytes the server received from the client
            long receivedBytes = countingInputStream.getBytesRead();
            System.out.println("Total bytes received from client: " + receivedBytes);
        }
        catch (EOFException e) {
            System.out.println("Client disconnected.");
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Client Handler exception: " + e.getMessage());
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


