package comms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConn {

    private static ServerSocket listener;
    private static Socket socket;

    public static void main(String[] argstem) throws Exception {
        System.err.println("Server is running");
        ServerConn conn = new ServerConn();
        conn.start();
    }

    public void start() throws IOException {
        ServerSocket listener = new ServerSocket(5000);
        try {
            while (true) {
                Socket sock = listener.accept();
                new ServerThread(socket).start();
            }
        } finally {
            listener.close();
        }
    }


}
