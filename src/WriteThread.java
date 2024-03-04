import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class WriteThread extends Thread {
    private BufferedReader input;

    private DataOutputStream out;


    public WriteThread(BufferedReader input, DataOutputStream out) {
        this.input = input;
        this.out = out;
    }

    @Override
    public void run() {
        while (true) {
            try {
                out.writeBytes(input.readLine() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
