import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            System.out.println("Serveren venter på klient");
            Socket connectionSocket = serverSocket.accept();
            (new ServerThread(connectionSocket)).start();
            System.out.println("Klient forbundet til Server");

        }
        // connection stuff
//        BufferedReader messageFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//        System.out.println("Klient forbundet til Server");
        // ----
//        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));




    }
}
