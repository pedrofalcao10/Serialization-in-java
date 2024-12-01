import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class jsonClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 55555;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            CountingOutputStream countingOutputStream = new CountingOutputStream(socket.getOutputStream());
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(countingOutputStream), true);

            int numPersons = 3;

            // Create a list of Person objects
            List<Person> personList = PersonManager.generatePersons(numPersons);
            System.out.println("Number of Persons: " + personList.size());

            // Serialize and send the list of Persons as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(personList);
            writer.println(jsonData);
            writer.flush();

            // Calculate bytes sent
            long totalBytesSent = countingOutputStream.getBytesWritten();
            System.out.println("Sent Person list as JSON:\n" + jsonData);
            System.out.println("Total bytes sent: " + totalBytesSent + " bytes");

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

