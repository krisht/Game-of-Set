package frontend;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


// Main class that creates the window of size and catches exceptions
public class Login {
    public static void main (String arg[]){
//        PrintStream dummyStream = new PrintStream(new OutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//            }
//        });
//        System.setErr(dummyStream);
//        System.setOut(dummyStream);
        try {
            LoginPage loginpage = new LoginPage();
            loginpage.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}