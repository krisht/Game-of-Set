import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConn {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public static void main(String[] args) throws Exception {
        ClientConn clientConn = new ClientConn();
        clientConn.connectToServer();
        clientConn.sendMessages();
    }

    public void connectToServer() throws Exception {
        System.err.println("frst");
        socket = new Socket("199.98.20.115", 5000);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("second");

        for (int ii = 0; ii < 3; ii++) {
            System.err.println(in.readLine());
        }
    }

    public void sendMessages() {

        Scanner scan = new Scanner(System.in);
        String input;

        while ((input = scan.nextLine()) != null) {
            this.out.println(input);
        }

    }
}
