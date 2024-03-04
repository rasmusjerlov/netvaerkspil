import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {

        // connection stuff
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Serveren venter på klient");
        Socket connectionSocket = serverSocket.accept();
        BufferedReader messageFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        System.out.println("Klient forbundet til Server");
        // ----

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        ReadThread readThread = new ReadThread(messageFromClient);
        WriteThread writeThread = new WriteThread(input, outToClient);

        readThread.start();
        writeThread.start();
    }
}
