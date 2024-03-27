import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            System.out.println("Serveren venter på klient");
            Socket connectionSocket = serverSocket.accept();
            ServerThread st = new ServerThread(connectionSocket);
            serverThreads.add(st);
            st.start();
            System.out.println("Klient forbundet til Server");

        }
        // connection stuff
//        BufferedReader messageFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//        System.out.println("Klient forbundet til Server");
        // ----
//        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    }
    public synchronized static void threadsForEach (String besked) {
        for (ServerThread thread : serverThreads) {
            thread.sendBesked(besked);
        }
    }

    public synchronized static void addPlayer(Player newPlayer) {
        for (ServerThread thread : serverThreads) {
            thread.sendBesked("new player " + newPlayer.getName());
        }
    }

}
