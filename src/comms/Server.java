package comms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    public static ConcurrentMap<Integer, Socket> socketList = new ConcurrentHashMap<>();
    public static BlockingQueue<String> bqueue = new LinkedBlockingQueue<>();
    public static ConcurrentMap<Integer, Socket> waitingSockets = new ConcurrentHashMap<>();

    private static int sid = 0;

    public static void main(String[] args) throws IOException {

        try {
            final int port = 8052;
            ServerSocket server = new ServerSocket(port);
            System.err.println("Waiting for client on: ");
            System.err.println("Hostname: " + InetAddress.getLocalHost().getHostName());
            System.err.println("Host IP : " + InetAddress.getLocalHost().getHostAddress());


        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

}
