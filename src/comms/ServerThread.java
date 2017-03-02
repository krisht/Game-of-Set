package comms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket sock;

    public ServerThread(Socket sock) {
        this.sock = sock;
        System.err.println("New client from: " + this.sock);
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

            out.println("Hello, you connected with 199.98.20.115:5000");

            while (true) {
                String input = in.readLine();
                if (input == null || input.equals("."))
                    break;
                out.println(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                sock.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            System.err.println("Connection with socket " + sock + " closed");
        }
    }

}

