import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class xmlServer {
    public static void main(String[] args) {
        final int PORT = 55555;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on PORT: " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("New client connected: " + connectionSocket.getInetAddress());

                // Handle client in a new thread
                new Thread(() -> handleClient(connectionSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public static void handleClient(Socket connectionSocket) {
        try {
            DataInputStream dataInputStream = new DataInputStream(connectionSocket.getInputStream());
            InputStream inputStream = connectionSocket.getInputStream();

            //Receive the length of the xml data
            int dataLength = dataInputStream.readInt();
            System.out.println("Received xml size: " + dataLength + " bytes");

            //Read the XML data from the client
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;

            //Read the input stream in chunks
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead));
            }

            System.out.println("bytes read: " + stringBuilder.length() + " bytes");

            String xmlData = stringBuilder.toString();
            System.out.println("Received XML data:\n" + xmlData);

            connectionSocket.close();
        }
        catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
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