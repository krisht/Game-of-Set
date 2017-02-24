package comms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SetServer {

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

            MessageProcessor proc = new MessageProcessor();
            Thread threadProc = new Thread(proc);

            threadProc.start();

            while (true) {
                Socket sock = server.accept();
                System.err.println("Connected from: ");
                System.err.println("Hostname: " + sock.getInetAddress().getHostName());
                System.err.println("Host IP : " + sock.getInetAddress().getHostAddress());

                sid++;

                waitingSockets.put(sid, sock);
                ServerThread st = new ServerThread(sock, sid);
                Thread newThread = new Thread(st);
                newThread.start();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }


    private static void sendMessage(int uid, String message)

}
