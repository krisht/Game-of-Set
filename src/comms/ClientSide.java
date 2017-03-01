import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConn {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public ClientConn() {
        System.out.println("Socket attempt");
        try {
            socket = new Socket("199.98.20.115", 5000);
            in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (exception e) {
            System.out.println("got rekt");
            System.exit(-1);
        }
        System.out.println("Client is set up!");
    }

    public static void main(String[] args) throws Exception {
        ClientConn clientConn = new ClientConn();
    }
}
