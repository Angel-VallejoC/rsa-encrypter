import javax.swing.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends JFrame {

    private final static String USER = "b966608a2edd06dc2b03cafaf673ebc0fdca0da76e3f882b22932cce41dac3c7";
    private final static String PASSWORD = "e5e6a685a76ab45d3ab362473e39cd4b69458cdbdad28247a9271cd47bd8a388";

    private final static int WINDOW_WIDTH = 300;
    private final static int WINDOW_HEIGHT = 200;
    private final static int MARGIN = 15;
    private final static int COMPONENT_HEIGHT = 25;

    private JTextField userInput, passwordInput;

    public Login(){
        super();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setTitle("RSA Encrypter/Decrypter");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Initializing components
        JLabel instLabel = new JLabel("Login to access the encrypter");
        JLabel userLabel = new JLabel("USER");
        JLabel passwordLabel = new JLabel("PASSWORD");

        userInput = new JTextField();
        passwordInput = new JPasswordField();
        passwordInput.addActionListener(event -> tryLogin());

        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(event -> tryLogin());


        // Setting components' positions
        instLabel.setBounds(MARGIN, MARGIN, 200, COMPONENT_HEIGHT );
        userLabel.setBounds(MARGIN, MARGIN * 3, 100, COMPONENT_HEIGHT);
        userInput.setBounds(MARGIN + 110, MARGIN * 3, 150, COMPONENT_HEIGHT );
        passwordLabel.setBounds(MARGIN, MARGIN * 5, 100, COMPONENT_HEIGHT);
        passwordInput.setBounds(MARGIN +110, MARGIN * 5, 150, COMPONENT_HEIGHT);
        loginButton.setBounds(MARGIN, MARGIN * 8, WINDOW_WIDTH - (MARGIN*2), COMPONENT_HEIGHT);

        // Adding components to the Frame
        add(instLabel);
        add(userLabel);
        add(userInput);
        add(passwordLabel);
        add(passwordInput);
        add(loginButton);

    }

    private void tryLogin() {
        String user = userInput.getText();
        String password = passwordInput.getText();

        String userSHA = null, passwordSHA = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(user.getBytes(StandardCharsets.UTF_8));
            userSHA = String.format("%064x", new BigInteger(1, digest.digest()));
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            passwordSHA = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "There was an error", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if ( !USER.equals(userSHA) || !PASSWORD.equals(passwordSHA)){
            JOptionPane.showMessageDialog(this, "User and/or password are incorrect", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            new RSAEncrypterDecrypter();
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }

}
