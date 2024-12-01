import java.io.*;
import java.net.Socket;
import java.util.List;

public class objClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 55555;

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server.");

            CountingOutputStream countingOutputStream = new CountingOutputStream(socket.getOutputStream());
            ObjectOutputStream output = new ObjectOutputStream(countingOutputStream);

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            int numPersons = 1;

            //Create and send a list of Person objects
            List<Person> personList = PersonManager.generatePersons(numPersons);

            //Send the list of Person objects
            long beforeBytes = countingOutputStream.getBytesWritten();
            output.writeObject(personList);
            output.flush();
            long afterBytes = countingOutputStream.getBytesWritten();

            System.out.println("Bytes sent for the list of Person objects: " + (afterBytes - beforeBytes));
            System.out.println("List with " + numPersons + " Person objects sent to server.");

            //Receive responses for each Person
            for (int i = 0; i < personList.size(); i++) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response from server: " + response);
                }
                catch (EOFException e) {
                    System.err.println("Server closed connection unexpectedly.");
                    break;
                }
            }

        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

class CountingOutputStream extends FilterOutputStream {
    private long bytesWritten = 0;

    public CountingOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        bytesWritten++;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        bytesWritten += len;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }
}

class CountingInputStream extends FilterInputStream {
    private long bytesRead = 0;

    public CountingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int byteRead = super.read();
        if (byteRead != -1) bytesRead++;
        return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int numBytes = super.read(b, off, len);
        if (numBytes != -1) bytesRead += numBytes;
        return numBytes;
    }

    public long getBytesRead() {
        return bytesRead;
    }
}
