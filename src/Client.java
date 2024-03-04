
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLOutput;


public class Client {

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.startClient();
    }

    public void startClient() throws IOException {
        String sentence;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Start a new thread to listen for messages from the server
        new Thread(() -> {
            try {
                String modifiedSentence;
                while ((modifiedSentence = inFromServer.readLine()) != null) {
                    System.out.println(modifiedSentence);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (!clientSocket.isClosed()) {
            System.out.println("Indtast et ord - eller 'stop' for at afslutte:");
            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence + '\n');
        }
    }
}