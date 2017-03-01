import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConn {

    public static void main(String[] args) throws Exception {
        System.out.println("Socket attempt");
        ServerSocket listener = new ServerSocket(5000);
        try {
            while (String temp = in.readline()) {
                BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("we saw " + temp);
            }
        } finally {
            listener.close();
        }
    }
}
