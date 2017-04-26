package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//test comment
public class LoginPage extends JFrame implements ActionListener{
    static ClientConnThreaded newConnectionThread;
    static int uid;
    static String username;
    static LandingPage landingPage;
    final int USER_NOT_EXIST = 1;
    final int PWD_INCORRECT = 2;
    final int USER_ALREADY_EXIST = 3;
    final int LOGIN_SUCCESS = 4;
    final int REGISTER_SUCCESS = 5;
    final int USER_ALREDY_LOGGED_IN = 6;
    JTextField usernameField_register;
    JTextField usernameField_login;
    JPasswordField passwordField_register, passwordField_login, repeatPasswordField_register;
    JButton SUBMIT, REGISTER, LOGINPANE, REGISTERPANE;
    JPanel InfoPanel, LoginPanel, RegisterPanel;
    JLabel usernameLabel_login, passwordLabel_login, repeatPasswordLabel_register,titleLabel;
    JLabel usernameLabel_register, passwordLabel_register;

    // Constructor that initialises and sets the labels to "username" and "password", initialises the fields, and
    // prepares the submit button. Then, creates the layout and sets the label.
    public LoginPage() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640, 480);
        Container cp = this.getContentPane();
        Font f = new Font("Arial",Font.BOLD, 60);
        cp.setLayout(new GridBagLayout());
        cp.setBackground(Color.decode("#009688"));
        
    	GridBagConstraints c = new GridBagConstraints();
    	GridBagConstraints c_title = new GridBagConstraints();
        titleLabel = new JLabel("SET");
    	InfoPanel = new JPanel(new GridBagLayout());
    	
    	c.anchor = GridBagConstraints.CENTER;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = 0;
    	c.gridy = 1;
    	
    	titleLabel.setFont(f);
    	titleLabel.setForeground(Color.WHITE);

    	c_title.anchor = GridBagConstraints.CENTER;
    	c_title.gridwidth = 1;
    	c_title.gridheight = 1;
    	c_title.gridx = 0;
    	c_title.gridy = 0;
    	c_title.insets = new Insets(8,4,12,20);
    	
    	cp.add(InfoPanel, c);
    	cp.add(titleLabel, c_title);
        // make the title
    	
        makeInfoPane(cp);
        
        uid = 0;

        // Prepare the action for the submit button
        // Set the title of the login window
        setTitle("Login for Game");

        newConnectionThread = new ClientConnThreaded();
        // username = "test";
        // enterLanding();
        // enterGameBoard();

    }
    
    
    private void makeInfoPane(Container cp){
        
        LOGINPANE = new JButton("LOGIN");
        REGISTERPANE = new JButton("REGISTER");
    	
    	LoginPanel = new JPanel(new GridBagLayout());
    	RegisterPanel = new JPanel(new GridBagLayout());
    	
    	GridBagConstraints c_loginpane = new GridBagConstraints();
    	GridBagConstraints c_registerpane = new GridBagConstraints();
    	GridBagConstraints c_infopane = new GridBagConstraints();
    	
		Font font = new Font("Arial",Font.PLAIN, 18);
		
		// set frontend features of color
    	LOGINPANE.setForeground(Color.white);
		LOGINPANE.setFocusPainted(false);
		LOGINPANE.setBackground(Color.decode("#80CBC4"));
		LOGINPANE.setBorderPainted(false);
		LOGINPANE.setFont(font);
		REGISTERPANE.setForeground(Color.white);
		REGISTERPANE.setFocusPainted(false);
		REGISTERPANE.setBackground(Color.decode("#4DB6AC"));
		REGISTERPANE.setBorderPainted(false);
		REGISTERPANE.setFont(font);
		
		InfoPanel.setBackground(Color.decode("#80CBC4"));
		
		// set frontend features of login and register panels
		LoginPanel.setBackground(Color.decode("#80CBC4"));
		RegisterPanel.setBackground(Color.decode("#80CBC4"));
		makeLoginPanel(LoginPanel, font);
		makeRegisterPanel(RegisterPanel, font);
		LoginPanel.setVisible(true);
		RegisterPanel.setVisible(false);

		c_loginpane.fill = GridBagConstraints.BOTH;
    	c_loginpane.gridwidth = 1;
    	c_loginpane.gridheight = 1;
    	c_loginpane.gridx = 0;
    	c_loginpane.gridy = 0;
    	c_loginpane.weightx = 0.5;

		c_registerpane.fill = GridBagConstraints.BOTH;
    	c_registerpane.gridwidth = 1;
    	c_registerpane.gridheight = 1;
    	c_registerpane.gridx = 1;
    	c_registerpane.gridy = 0;
    	c_registerpane.weightx = 0.5;

		c_infopane.fill = GridBagConstraints.BOTH;
    	c_infopane.gridwidth = 2;
    	c_infopane.gridheight = 1;
    	c_infopane.gridx = 0;
    	c_infopane.gridy = 1;
    	
    	InfoPanel.add(LOGINPANE, c_loginpane);
    	InfoPanel.add(REGISTERPANE, c_registerpane);
    	InfoPanel.add(LoginPanel, c_infopane);
    	InfoPanel.add(RegisterPanel,c_infopane);
    	
    	LOGINPANE.addActionListener(this);
    	REGISTERPANE.addActionListener(this);
    }
    
    private void makeLoginPanel(Container cp, Font font){
    	usernameField_login = new JTextField(15);
    	passwordField_login = new JPasswordField(15);
        SUBMIT = new JButton("SUBMIT");
        usernameLabel_login = new JLabel("Username: ");
        passwordLabel_login = new JLabel("Password: ");
    	GridBagConstraints c_uname = new GridBagConstraints();
    	GridBagConstraints c_pwd = new GridBagConstraints();
    	GridBagConstraints c_submit = new GridBagConstraints();
    	GridBagConstraints c_uname_label = new GridBagConstraints();
    	GridBagConstraints c_pwd_label = new GridBagConstraints();
        usernameField_login.setPreferredSize( new Dimension( 160, 30 ) );
        passwordField_login.setPreferredSize( new Dimension( 160, 30 ) );

		Font font2 = new Font("Arial",Font.PLAIN, 13);
        usernameField_login.setFont(font2);
        passwordField_login.setFont(font2);
        usernameLabel_login.setForeground(Color.WHITE);
        passwordLabel_login.setForeground(Color.WHITE);
        usernameField_login.setBorder(null);
        passwordField_login.setBorder(null);
        SUBMIT.setOpaque(true);
		// SUBMIT.setForeground(Color.white);
		// SUBMIT.setFocusPainted(false);
		SUBMIT.setBackground(Color.decode("#FF4081"));
		// SUBMIT.setFont(font);
        
    	c_uname_label.gridwidth = 1;
    	c_uname_label.gridheight = 1;
    	c_uname_label.gridx = 0;
    	c_uname_label.gridy = 0;
    	c_uname_label.insets = new Insets(32,20,8,4);

    	c_pwd_label.gridwidth = 1;
    	c_pwd_label.gridheight = 1;
    	c_pwd_label.gridx = 0;
    	c_pwd_label.gridy = 1;
    	c_pwd_label.insets = new Insets(8,20,8,4);
		
    	c_uname.gridwidth = 1;
    	c_uname.gridheight = 1;
    	c_uname.gridx = 1;
    	c_uname.gridy = 0;
    	c_uname.insets = new Insets(32,4,8,20);

    	c_pwd.gridwidth = 1;
    	c_pwd.gridheight = 1;
    	c_pwd.gridx = 1;
    	c_pwd.gridy = 1;
    	c_pwd.insets = new Insets(8,4,8,20);

    	c_submit.gridwidth = 2;
    	c_submit.gridheight = 1;
    	c_submit.gridx = 0;
    	c_submit.gridy = 2;
    	c_submit.insets = new Insets(8,0,16,0);
    	
    	cp.add(usernameField_login,c_uname);
    	cp.add(passwordField_login,c_pwd);
    	cp.add(usernameLabel_login,c_uname_label);
    	cp.add(passwordLabel_login,c_pwd_label);
    	cp.add(SUBMIT,c_submit);
    	
        SUBMIT.addActionListener(this);
        SUBMIT.setVisible(true);
    	
    }
    
    private void makeRegisterPanel(Container cp, Font font){
    	usernameField_register = new JTextField(15);
        passwordField_register = new JPasswordField(15);
        repeatPasswordField_register = new JPasswordField(15);
        REGISTER = new JButton("REGISTER");
        usernameLabel_register = new JLabel("Username: ");
        passwordLabel_register = new JLabel("Password: ");
        repeatPasswordLabel_register = new JLabel("Repeat Password: ");
        
        GridBagConstraints c_uname = new GridBagConstraints();
    	GridBagConstraints c_pwd = new GridBagConstraints();
    	GridBagConstraints c_pwd_repeat = new GridBagConstraints();
    	GridBagConstraints c_register = new GridBagConstraints();
    	GridBagConstraints c_uname_label = new GridBagConstraints();
    	GridBagConstraints c_pwd_label = new GridBagConstraints();
    	GridBagConstraints c_pwd_repeat_label = new GridBagConstraints();
        usernameField_register.setPreferredSize( new Dimension( 160, 30 ) );
        passwordField_register.setPreferredSize( new Dimension( 160, 30 ) );
        repeatPasswordField_register.setPreferredSize( new Dimension(160, 30));
        
		Font font2 = new Font("Arial",Font.PLAIN, 13);
        usernameField_register.setFont(font2);
        passwordField_register.setFont(font2);
        repeatPasswordField_register.setFont(font2);
        usernameLabel_register.setForeground(Color.WHITE);
        passwordLabel_register.setForeground(Color.WHITE);
        repeatPasswordLabel_register.setForeground(Color.WHITE);
        usernameField_register.setBorder(null);
        passwordField_register.setBorder(null);
        repeatPasswordField_register.setBorder(null);
		REGISTER.setForeground(Color.white);
		REGISTER.setFocusPainted(false);
		REGISTER.setBackground(Color.decode("#FF4081"));
		REGISTER.setFont(font);
        
    	c_uname_label.gridwidth = 1;
    	c_uname_label.gridheight = 1;
    	c_uname_label.gridx = 0;
    	c_uname_label.gridy = 0;
    	c_uname_label.insets = new Insets(32,20,8,4);

    	c_pwd_label.gridwidth = 1;
    	c_pwd_label.gridheight = 1;
    	c_pwd_label.gridx = 0;
    	c_pwd_label.gridy = 1;
    	c_pwd_label.insets = new Insets(8,20,8,4);

    	c_pwd_repeat_label.gridwidth = 1;
    	c_pwd_repeat_label.gridheight = 1;
    	c_pwd_repeat_label.gridx = 0;
    	c_pwd_repeat_label.gridy = 2;
    	c_pwd_repeat_label.insets = new Insets(8,20,8,4);
		
    	c_uname.gridwidth = 1;
    	c_uname.gridheight = 1;
    	c_uname.gridx = 1;
    	c_uname.gridy = 0;
    	c_uname.insets = new Insets(32,4,8,20);

    	c_pwd.gridwidth = 1;
    	c_pwd.gridheight = 1;
    	c_pwd.gridx = 1;
    	c_pwd.gridy = 1;
    	c_pwd.insets = new Insets(8,4,8,20);
    	
    	c_pwd_repeat.gridwidth = 1;
    	c_pwd_repeat.gridheight = 1;
    	c_pwd_repeat.gridx = 1;
    	c_pwd_repeat.gridy = 2;
    	c_pwd_repeat.insets = new Insets(8,4,8,20);

    	c_register.gridwidth = 2;
    	c_register.gridheight = 1;
    	c_register.gridx = 0;
    	c_register.gridy = 3;
    	c_register.insets = new Insets(8,0,16,0);
    	
    	cp.add(usernameField_register,c_uname);
    	cp.add(passwordField_register,c_pwd);
    	cp.add(repeatPasswordField_register,c_pwd_repeat);
    	cp.add(usernameLabel_register,c_uname_label);
    	cp.add(passwordLabel_register,c_pwd_label);
    	cp.add(repeatPasswordLabel_register, c_pwd_repeat_label);
    	cp.add(REGISTER,c_register);

        REGISTER.addActionListener(this);
    }
    
    // Checks username and password, and logs in if both present, throws error else.
    public void actionPerformed(ActionEvent ae) {

        // Fetch the username and password from the textboxes
        // Check which button is pressed
        JButton b = (JButton) ae.getSource();
        if (b.equals(REGISTER)) {
            String username = usernameField_register.getText();
            String value2 = passwordField_register.getText();
            String value3 = repeatPasswordField_register.getText();


            try {
                value2 = hashPassword(value2);
                value3 = hashPassword(value3);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }



            // check that passwords are equal
            if ((username != null && !username.isEmpty()) && (value2 != null && !value2.isEmpty()) &&
                    value3 != null && !value3.isEmpty()) {
                if (!value2.equals(value3)) {
                    System.out.println("Passwords are not the same");
                    JOptionPane.showMessageDialog(this, "Passwords are not the same", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // check availability

                    switch (newConnectionThread.registerUser(username, value2)) {
                        case REGISTER_SUCCESS:
                            System.out.println("Registration successful!");
                            JOptionPane.showMessageDialog(this, "Registration Successful", "Success", JOptionPane.PLAIN_MESSAGE);
                            enterLanding();
                            break;
                        case USER_ALREADY_EXIST:
                            System.out.println("ERROR: User already exists!");
                            JOptionPane.showMessageDialog(this, "User already exist in database", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        default:
                            break;

                    }
                }
            } else {
                System.out.println("username or password not present");
                JOptionPane.showMessageDialog(this, "Username or password not present", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (b.equals(SUBMIT)) {
            // Check that both fields are present
        	username = usernameField_login.getText();
            String value2 = passwordField_login.getText();

            try {
                value2 = hashPassword(value2);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }





            if ((username != null && !username.isEmpty()) && (value2 != null && !value2.isEmpty())) {
                switch (newConnectionThread.loginUser(username, value2)) {

                    case USER_NOT_EXIST:
                        System.out.println("ERROR: User does not exist.");
                        JOptionPane.showMessageDialog(this, "User does not exist in database", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case PWD_INCORRECT:
                        System.out.println("ERROR: Password is incorrect.");
                        JOptionPane.showMessageDialog(this, "Username and password do not match", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case LOGIN_SUCCESS:
                        System.out.println("Login successful!");
                        JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.PLAIN_MESSAGE);
                        enterLanding();
                        break;
                    case USER_ALREDY_LOGGED_IN:
                        System.out.print("User already logged in");
                        JOptionPane.showMessageDialog(this, "User already logged in", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        break;
                }
            } else {    // Else, throw an error in an error box
                System.out.println("username and/or password not present");
                JOptionPane.showMessageDialog(this, "Username or password not present", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else if (b.equals(LOGINPANE)) {
        	RegisterPanel.setVisible(false);
        	LoginPanel.setVisible(true);
    		LOGINPANE.setBackground(Color.decode("#80CBC4"));
    		REGISTERPANE.setBackground(Color.decode("#4DB6AC"));
        }else if (b.equals(REGISTERPANE)) {
        	LoginPanel.setVisible(false);
        	RegisterPanel.setVisible(true);
    		REGISTERPANE.setBackground(Color.decode("#80CBC4"));
    		LOGINPANE.setBackground(Color.decode("#4DB6AC"));
        }
    }


    private String hashPassword(String password) throws NoSuchAlgorithmException {
        //Hashing function
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b) {
            sb.append(Integer.toHexString(b1 & 0xff));
        }
        return sb.toString();
    }

    private void enterLanding() {
        System.out.println("logged in");

        try {

            // Create a landing page
             landingPage = new LandingPage();

            // NOTE: The proper way as implemented in the landing page closes the landing page too, so use this way
            this.setVisible(false);
            this.dispose();

            // Make page visible
            landingPage.setVisible(true);
            // Set title
            landingPage.setTitle("Welcome " + username);

            // Set uername in login window
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void enterGameBoard(){
    	try {

            // Create a landing page
             GameBoard_Front gb = new GameBoard_Front();

            // NOTE: The proper way as implemented in the landing page closes the landing page too, so use this way
            this.setVisible(false);
            this.dispose();

            // Make page visible
            gb.setVisible(true);
            // Set title
            gb.setTitle("Welcome " + username);

            // Set uername in login window
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
}