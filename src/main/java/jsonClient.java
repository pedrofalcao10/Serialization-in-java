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

            int numPersons = 200;

            // Create a list of Person objects
            List<Person> personList = PersonManager.generatePersons(numPersons);
            System.out.println("Number of Persons: " + personList.size());

            // Serialize and send the list of Persons as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(personList);
            byte[] jsonBytes = jsonData.getBytes();

            // Calculate the size of the serialized JSON data
            int dataLength = jsonBytes.length;

            // Create a socket and send the data
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Send the length of the JSON first (4 bytes)
            dataOutputStream.writeInt(dataLength);

            // Send the JSON data
            dataOutputStream.write(jsonBytes);
            System.out.println("Sent JSON data: " + jsonData);
            System.out.println("Serialized JSON size: " + dataLength + " bytes");

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

