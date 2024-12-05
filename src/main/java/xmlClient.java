import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class xmlClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 55555;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            CountingOutputStream countingOutputStream = new CountingOutputStream(socket.getOutputStream());
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(countingOutputStream), true);

            int numPersons = 20000;

            //Create a list of Person objects
            List<Person> personList = PersonManager.generatePersons(numPersons);
            //System.out.println("length: " + personList.size());

            // Serialize object to XML
            XmlMapper xmlMapper = new XmlMapper();
            String xmlData = xmlMapper.writeValueAsString(personList);
            byte[] xmlBytes = xmlData.getBytes();

            // Calculate the size of the serialized XML data
            int serializedSize = xmlBytes.length;

            // Send the XML data over a socket
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Send the XML data
            dataOutputStream.write(xmlBytes);

            System.out.println("Sent XML data:\n" + xmlData);
            System.out.println("Serialized XML size: " + serializedSize + " bytes");
        }
        catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}