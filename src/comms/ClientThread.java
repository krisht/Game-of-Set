package comms;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by krishna on 2/23/17.
 */
public class ClientThread implements Runnable {

    Socket sock;
    Scanner input;
    Client c;
    boolean completeThread = false;

    public ClientThread(Socket sock) {
        this.sock = sock;
    }

    public ClientThread(Socket sock, Client c) {
        this.sock = sock;
        this.c = c;
    }

}
