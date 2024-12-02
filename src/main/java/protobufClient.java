import com.example.protobuf.PersonProto.Person;
import com.example.protobuf.PersonProto.PersonList;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class protobufClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 55555;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            OutputStream outputStream = socket.getOutputStream();

            // Generate a list of custom Person objects
            PersonList.Builder personListBuilder = PersonList.newBuilder();

            int numPersons = 3;

            // Generate Person instances
            for (int i = 0; i < numPersons; i++) {
                Person person = Person.newBuilder()
                        .setName("Person" + (i + 1))
                        .setLocalization("Location" + (i + 1))
                        .setAge(20 + i)
                        .setNumID(java.util.UUID.randomUUID().toString())
                        .build();
                personListBuilder.addPersons(person);
            }

            // // Serialize the Protobuf data and get its size
            PersonList personList = personListBuilder.build();
            byte[] serializedData = personList.toByteArray();
            int dataLength = serializedData.length;

            // Send data
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.write(serializedData);
            System.out.println("Sent serialized Protobuf data. Size: " + dataLength + " bytes");

            // Close the socket
            dataOutputStream.close();
            socket.close();

            System.out.println("PersonList sent to server.");
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}