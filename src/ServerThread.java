import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
			String message = String.valueOf(clientID) + "\n";
			outToClient.writeBytes(message);
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
			//while ((message = inFromClient.readLine()) != null) {

			//}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
