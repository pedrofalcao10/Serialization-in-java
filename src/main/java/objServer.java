import java.io.*;
import java.net.*;
import java.util.List;

public class objServer {
    public static void main(String[] args){
        final int PORT = 55555;

        try(ServerSocket listenSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = listenSocket.accept();
                System.out.println("New client connected: " + connectionSocket.getInetAddress());

                //Handle client in a new thread
                new Thread(() -> handleClient(connectionSocket)).start();
            }
        }
        catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public static void handleClient(Socket connectionSocket) {
        try (
                CountingInputStream countingInputStream = new CountingInputStream(connectionSocket.getInputStream());
                ObjectInputStream input = new ObjectInputStream(countingInputStream);

                CountingOutputStream countingOutputStream = new CountingOutputStream(connectionSocket.getOutputStream());
                ObjectOutputStream output = new ObjectOutputStream(countingOutputStream)
        ) {
            // Receive a list of Person objects
            Object receivedObject = input.readObject();

            if (receivedObject instanceof List<?>) {
                //@SuppressWarnings("unchecked")
                List<Person> personList = (List<Person>) receivedObject;

                long bytesInResponse = 0;
                //Send a response back to the client and counting sent bytes
                for (Person person : personList) {
                    System.out.println(person);

                    // Send a response back to the client
                    String response = "Hello, " + person.getName() + "! Your message has ID " + person.getNumID();
                    long beforeBytesSent = countingOutputStream.getBytesWritten();
                    try {
                        output.writeObject(response);
                        output.flush();
                    }
                    catch (IOException e) {
                        System.err.println("Error sending response to client: " + e.getMessage());
                        break; // Stop writing if the connection is closed
                    }
                    long afterBytesSent = countingOutputStream.getBytesWritten();
                    bytesInResponse += (afterBytesSent - beforeBytesSent);
                }
                System.out.println("Bytes sent from server to client in response: " + bytesInResponse);
            }

            // Measure how many bytes the server received from the client
            long receivedBytes = countingInputStream.getBytesRead();
            System.out.println("Total bytes received from client: " + receivedBytes);
        }
        catch (EOFException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Client Handler exception: " + e.getMessage());
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