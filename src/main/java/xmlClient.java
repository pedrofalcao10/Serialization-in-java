import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class xmlClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 65432;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            CountingOutputStream countingOutputStream = new CountingOutputStream(socket.getOutputStream());
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(countingOutputStream), true);

            int numPersons = 3;

            //Create a list of Person objects
            List<Person> personList = PersonManager.generatePersons(numPersons);
            System.out.println("length: " + personList.size());

            //Serialize and send each Person as XML
            XmlMapper xmlMapper = new XmlMapper();
            long totalBytesSent = 0;

            for (Person person : personList) {
                String xmlData = xmlMapper.writeValueAsString(person);
                writer.println(xmlData); //Send XML data to server
                writer.flush();

                long bytesSentForPerson = countingOutputStream.getBytesWritten() - totalBytesSent;
                totalBytesSent += bytesSentForPerson;

                System.out.println("Sent Person as XML: " + xmlData);
                System.out.println("Bytes sent for this Person: " + bytesSentForPerson + " bytes");
            }

            System.out.println("Total bytes sent for all Persons: " + totalBytesSent + " bytes");

        }
        catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
