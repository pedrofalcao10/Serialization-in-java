import com.example.protobuf.PersonProto.Person;
import com.example.protobuf.PersonProto.PersonList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class protobufServer {
    public static void main(String[] args) {
        final int PORT = 55555;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("New client connected: " + connectionSocket.getInetAddress());

                new Thread(() -> handleClient(connectionSocket)).start();
            }
        }
        catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    private static void handleClient(Socket connectionSocket) {
        try (
                InputStream inputStream = connectionSocket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
        ) {
            // Read and parse the Protobuf message
            PersonList personList = PersonList.parseFrom(inputStream);

            for (Person person : personList.getPersonsList()) {
                System.out.println("Received Person:");
                System.out.println("Name: " + person.getName());
                System.out.println("Localization: " + person.getLocalization());
                System.out.println("Age: " + person.getAge());
                System.out.println("NumID: " + person.getNumID());
                System.out.println("----------------------------------------------");
            }
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