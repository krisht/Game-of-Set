import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by abhinav on 2/3/2017.
 */
public class RegisterPage extends JFrame{
	public RegisterPage(){
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(new JLabel("This is by Brenda"));
		cp.add(new JButton("Button"));
	}
}
