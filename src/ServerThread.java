import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    Socket connSocket;

    HashMap map;

    public ServerThread(Socket connSocket, HashMap map) {
        this.connSocket = connSocket;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            System.out.println("Connected");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

            String navnFraClient = inFromClient.readLine().toLowerCase();
            outToClient.writeBytes((String) map.get(navnFraClient) + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
