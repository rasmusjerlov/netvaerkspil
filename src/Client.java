import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {

	public static void main(String[] args) throws Exception, IOException {

		Socket clientSocket = new Socket("10.10.138.168", 9999);
		BufferedReader messageFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		// ----

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

//		ReadThread readThread = new ReadThread(messageFromServer);
//		WriteThread writeThread = new WriteThread(input, outToServer);
//
//		readThread.start();
//		writeThread.start();
	}
}
