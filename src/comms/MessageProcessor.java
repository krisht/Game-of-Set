package comms;

/**
 * Created by krishna on 2/23/17.
 */
public class MessageProcessor implements Runnable {

    public void run() {
        String message;

        while (true) {
            try {
                message = Server.bqueue.take();
                try {
                    processMessage(message);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }

    public String processMessage(String message) {


        return null;
    }

}
