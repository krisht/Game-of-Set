import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConn {

    private static ServerSocket listener;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        System.out.println("Socket attempt");
        try {
            String temp;
            BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while ((temp = in.readLine()) != null) {
                out.println("we saw " + temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch(Exception e) {
                System.out.println("ehhhhh not working");
            }
        }
    }

    public ServerConn() throws Exception {
        ServerConn serverConn = new ServerConn();
        listener = new ServerSocket(5000);
        socket = listener.accept();
    }

}
