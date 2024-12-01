import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
        try (
                CountingInputStream countingInputStream = new CountingInputStream(connectionSocket.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(countingInputStream));
        ) {
            //Read JSON data from the client
            StringBuilder jsonDataBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonDataBuilder.append(line).append("\n");
            }
            String jsonData = jsonDataBuilder.toString();
            System.out.println("Received JSON data:\n" + jsonData + "\n");

            //Measure bytes received
            long receivedBytes = countingInputStream.getBytesRead();
            System.out.println("Total bytes received from client: " + receivedBytes);

            //Deserialize JSON into a list of Person objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<Person> personList = objectMapper.readValue(jsonData, new TypeReference<List<Person>>() {});

            System.out.println("Deserialized objects:");
            for (Person person : personList) {
                System.out.println(person);
            }

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