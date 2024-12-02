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
            DataOutputStream output = new DataOutputStream(countingOutputStream);

            // Generate and send Person objects one field at a time
            List<Person> personList = PersonManager.generatePersons(3);

            for (Person person : personList) {
                output.writeUTF(person.getName());           // Send name (String)
                System.out.println("Sent Name: " + person.getName());

                output.writeUTF(person.getLocalization());  // Send localization (String)
                System.out.println("Sent Localization: " + person.getLocalization());

                output.writeInt(person.getAge());           // Send age (int)
                System.out.println("Sent Age: " + person.getAge());

                output.writeUTF(person.getNumID());         // Send numID (int)
                System.out.println("Sent NumID: " + person.getNumID());

                System.out.println("-----------------------");
            }

            // Calculate and print total bytes sent
            long totalBytes = countingOutputStream.getBytesWritten();
            System.out.println("Total bytes sent: " + totalBytes);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

