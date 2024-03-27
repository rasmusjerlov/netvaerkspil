import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;

public class ServerThread extends Thread {
    Socket connSocket;

    public ServerThread(Socket connSocket) {
        this.connSocket = connSocket;

    }

    // ServerThread.java
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

            String posMessage;
            while (true) {
                posMessage = inFromClient.readLine();
                if (posMessage.startsWith("new player joined")) {
                    String[] parts = posMessage.split(" ");
                    String playerName = parts[3];
                    Player newPlayer = new Player(playerName, 0, 0, "up");
                    Server.addPlayer(newPlayer);
                } else {
                    Server.threadsForEach(posMessage);
                }
                System.out.println(posMessage); //Debug besked
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + e.getMessage());
            try {
                connSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBesked(String besked) {
        try {
            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
            String message = besked;
            outToClient.writeBytes(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
