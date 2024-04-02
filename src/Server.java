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
            ServerThread st = new ServerThread(connectionSocket); // Opretter ny servertråd ved ny forbindelse
            serverThreads.add(st); //Tilføjer servertråden til en liste til senere gennemløb
            st.start();
            System.out.println("Klient forbundet til Server");

        }
    }
    public synchronized static void threadsForEach (String besked) {
        for (ServerThread thread : serverThreads) { //Sender en besked fra en servertråd ud til de andre servertråde
            thread.sendBesked(besked); //Metode som sender besked fra tråd til klient
        }
    }

}
