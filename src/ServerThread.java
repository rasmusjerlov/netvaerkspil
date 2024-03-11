import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
	Socket connSocket;
	
	public ServerThread(Socket connSocket) {
		this.connSocket = connSocket;
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
			String message;
			while ((message = inFromClient.readLine()) != null) {

			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
