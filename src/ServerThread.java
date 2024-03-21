import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class ServerThread extends Thread{
	Socket connSocket;
	private int clientID;
	
	public ServerThread(Socket connSocket, int clientID) {
		this.connSocket = connSocket;
		this.clientID = clientID;

	}
	public void run() {
    try {
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
        String idmessage = String.valueOf(clientID) + "\n";
        outToClient.writeBytes(idmessage);

        String posMessage;
        while (true) {
            posMessage = inFromClient.readLine();
            Server.threadsForEach(posMessage);
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
