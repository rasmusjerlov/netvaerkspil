import java.io.BufferedReader;
import java.io.IOException;

public class ReadThread extends Thread {
    private BufferedReader message;

    public ReadThread(BufferedReader message) {
        this.message = message;
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println(message.readLine() + "\r");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
