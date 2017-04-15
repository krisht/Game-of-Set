package backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

class ServerConn {

    static ConcurrentHashMap<Integer, Socket> uidToSocket = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer, CopyOnWriteArraySet> gidToUid = new ConcurrentHashMap<>();

    private static ServerSocket listener;
    private static Socket socket;

    public static void main(String[] args) throws Exception {
        System.err.println("Server is running");
        ServerConn conn = new ServerConn();
        conn.start();
    }

    private void start() throws IOException {
        try {
            ServerSocket listener = new ServerSocket(5000);

            while (true) { //needs to throw exception some how
                Socket sock = listener.accept();
                ServerThread new_thread = new ServerThread(sock);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

