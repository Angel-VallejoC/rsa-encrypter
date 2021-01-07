import javax.crypto.Cipher;
import java.security.*; 

public class RSA {

    private static final String RSA = "RSA";

    // Generating public & private keys using RSA algorithm.
    public static KeyPair generateRSAKeyPair() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(4096, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    // Encryption function which converts the plainText into a cipherTex
    public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes());
    }

    // Decryption function which converts the cipher text back to the original plaintext.
    public static String decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(cipherText);
        return new String(result);
    }

}
