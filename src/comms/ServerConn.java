package comms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConn {

    private static ServerSocket listener;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        System.err.println("Server is running");
        ServerConn conn = new ServerConn();
        conn.start();
    }

    public void start() throws IOException {
        try {

            ServerSocket listener = new ServerSocket(5000);

            while (true) {
                Socket sock = listener.accept();
                ServerThread new_thread = new ServerThread(sock);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

