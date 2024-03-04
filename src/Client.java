import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {

	public static void main(String[] args) throws Exception, IOException {

		// connection stuff
		Socket navneSocket = new Socket("10.10.138.146", 6789);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Skriv et navn");
		String name = reader.readLine();

		DataOutputStream outToNavneServer = new DataOutputStream(navneSocket.getOutputStream());
		BufferedReader messageFromNameServer = new BufferedReader(new InputStreamReader(navneSocket.getInputStream()));

		outToNavneServer.writeBytes(name + "\n");

		String receivedIP = messageFromNameServer.readLine();

		navneSocket.close();
		System.out.println("Navneserver er termineret - forbinder til: " + receivedIP);

		Socket clientSocket = new Socket(receivedIP, 6789);
		BufferedReader messageFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		// ----

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		ReadThread readThread = new ReadThread(messageFromServer);
		WriteThread writeThread = new WriteThread(input, outToServer);

		readThread.start();
		writeThread.start();
	}
}
