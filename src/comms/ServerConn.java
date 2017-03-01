import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConn {

    private static ServerSocket listener;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        ServerConn serverConn = new ServerConn();
        System.out.println("Socket attempt");
        try {
            String temp;
            BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                System.out.println("IN LOOP");
                out.println("we saw: "+ in.readLine());
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

    public ServerConn() {
        try {
            System.out.println("1st");
            listener = new ServerSocket(5000);
            System.out.println("2nd");
            socket = listener.accept();
            System.out.println("3rd");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ugotfuktboi");
        }
    }

}
