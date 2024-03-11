import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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


			// Do the work and the communication with the client here
			// The following two lines are only an example
			String[] pos = inFromClient.readLine().split("\\s++");
			String posMessage;
			while ((posMessage = Arrays.toString(inFromClient.readLine().split("\\s++"))) != null) {
				outToClient.writeBytes(posMessage + "\n");
				System.out.println(posMessage);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
