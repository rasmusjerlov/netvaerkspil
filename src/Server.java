

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Serveren venter på klient");
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        System.out.println("Klient forbundet til Server");

        // Start a new thread to listen for messages from the client
        new Thread(() -> {
            try {
                String clientSentence;
                while ((clientSentence = inFromClient.readLine()) != null) {
                    if (!clientSentence.equals("stop")) {
                        System.out.println(clientSentence);
                    } else {
                        System.out.println("Klienten har afsluttet forbindelsen");
                        connectionSocket.close();
                        welcomeSocket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (!connectionSocket.isClosed()) {
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Indtast et ord - eller 'stop' for at afslutte:");
            String capitalizedSentence = fromServer.readLine() + '\n';
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}