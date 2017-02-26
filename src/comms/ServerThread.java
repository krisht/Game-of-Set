package comms;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread implements Runnable {

    Socket sock;
    int sid;
    String message = "";
    private Scanner input;
    private PrintWriter out;

    public void run() {
        try {
            input = new Scanner(sock.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (true) {
            if (!input.hasNext() || !sock.isConnected())
                break;

            message = input.nextLine();

            if (message != null) {

            }

        }
    }


}
