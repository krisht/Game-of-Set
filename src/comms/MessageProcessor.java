package comms;

public class MessageProcessor implements Runnable {

    public void run() {
        String message;

        while (true) {
            try {
                message = SetServer.bqueue.take();
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
