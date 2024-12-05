import java.io.*;
import java.net.Socket;
import java.util.List;

public class customClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 55555;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            CountingOutputStream countingOutputStream = new CountingOutputStream(socket.getOutputStream());
            ObjectOutputStream output = new ObjectOutputStream(countingOutputStream);

            int numPersons = 20000;

            // Create and send individual fields for each Person
            List<Person> personList = PersonManager.generatePersons(numPersons);

            long totalBytesSent = 0;
            for (Person person : personList) {
                System.out.println("Sending Person: " + person);

                output.writeObject(person.getName()); // Send name (String)
                System.out.println("  Name (String): " + person.getName());

                output.writeObject(person.getLocalization()); // Send localization (String)
                System.out.println("  Localization (String): " + person.getLocalization());

                output.writeObject(person.getAge()); // Send age (int32)
                System.out.println("  Age (int32): " + person.getAge());

                output.writeObject(person.getNumID()); // Send numID (String)
                System.out.println("  numID (String): " + person.getNumID());

                System.out.println("----------------------------------------------");

                totalBytesSent = countingOutputStream.getBytesWritten();
            }

            System.out.println("Total bytes sent for all Persons: " + totalBytesSent + " bytes");
        }
        catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

