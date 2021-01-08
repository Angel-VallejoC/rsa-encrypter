import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAEncrypterDecrypter extends JFrame {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private JButton encryptButton, decryptButton;

    public RSAEncrypterDecrypter(){
        super();
        setSize(350, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("RSA Encrypter/Decrypter");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        initComponents();
        setVisible(true);

        new Thread(this::generateKeys).start();
    }

    private void initComponents() {
        encryptButton = new JButton("ENCRYPT");
        encryptButton.setMaximumSize( new Dimension(200, 30));
        encryptButton.setEnabled(false);
        decryptButton = new JButton("DECRYPT");
        decryptButton.setMaximumSize( new Dimension(200, 30));
        decryptButton.setEnabled(false);

        encryptButton.addActionListener(e -> encrypt());
        decryptButton.addActionListener(e -> decrypt());

        encryptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        decryptButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0,20)));
        add(encryptButton);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(decryptButton);
    }

    private void generateKeys() {
        System.out.println("Generating public and private keys...");
        JOptionPane.showMessageDialog(this, "Generating public and private keys, please wait", "Generating keys", JOptionPane.INFORMATION_MESSAGE);

        KeyPair keyPair;
        try {
            keyPair = RSA.generateRSAKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            System.out.println("Public key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println("Private key: " + Base64.getEncoder().encodeToString( privateKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Couldn't create keys", "Error", JOptionPane.ERROR_MESSAGE);
        }

        encryptButton.setEnabled(true);
        decryptButton.setEnabled(true);
    }

    private void encrypt() {
        if (publicKey == null) {
            JOptionPane.showMessageDialog(this, "Unable to encrypt message. Try again", "Public key does not exist", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File selectedFile = selectFile();
        if (selectedFile == null)   return;

        try {
            System.out.println("Encrypting...");

            String data = new String(Files.readAllBytes(selectedFile.toPath()));
            if (data.trim().equals("")) return;

            byte[] cipherText = RSA.encrypt(data, publicKey);
            byte[] encrypted = Base64.getEncoder().encode(cipherText);

            saveEncryptedFile( encrypted, selectedFile.getParent() + "\\encrypted.rsa");

            System.out.println("Content successfully encrypted");
            JOptionPane.showMessageDialog(this, "File successfully encrypted, encrypted file: " + selectedFile.getParent() + "\\encrypted.rsa",
                    "Successfully encrypted", JOptionPane.INFORMATION_MESSAGE);

            showContent("Encrypted content", new String(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "There was an error, couldn't encrypt message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decrypt() {
        if (privateKey == null) {
            JOptionPane.showMessageDialog(this, "Unable to decrypt message", "Private key does not exist", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File selectedFile = selectFile();
        if (selectedFile == null)   return;

        try {
            System.out.println("Decrypting...");

            byte[] data = Files.readAllBytes(selectedFile.toPath());
            byte[] cipherText = Base64.getDecoder().decode(data);

            String decryptedText = RSA.decrypt(cipherText, privateKey);

            System.out.println("Content successfully decrypted");
            JOptionPane.showMessageDialog(this, "File successfully decrypted", "Successfully decrypted", JOptionPane.INFORMATION_MESSAGE);

            showContent("Decrypted content", decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "There was an error, couldn't decrypt message",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }

        return null;
    }

    private void saveEncryptedFile(byte[] encryptedData, String path) throws IOException {
        File encryptedFile = new File(path);
        if (!encryptedFile.exists() && !encryptedFile.createNewFile()) {
            JOptionPane.showMessageDialog(this, "File couldn't be saved", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Files.write(encryptedFile.toPath(), encryptedData);
    }

    private void showContent(String windowTitle, String content) {
        JFrame result = new JFrame(windowTitle);
        result.setSize(300, 300);
        result.setLocationRelativeTo(this);
        result.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(10, 10);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setText(content);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        result.add(scrollPane);
        result.setVisible(true);
    }


}
