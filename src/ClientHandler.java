import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket connectionSocket;

    public ClientHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader messageFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            System.out.println("Klient forbundet til Server");

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            ReadThread readThread = new ReadThread(messageFromClient);
            WriteThread writeThread = new WriteThread(input, outToClient);

            readThread.start();
            writeThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}