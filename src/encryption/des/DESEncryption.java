package encryption.des;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class DESEncryption {
    private Cipher encryptionCipher;
    private Cipher decryptionCipher;
    private SecretKey secretKey;


    public void decryptTheFile(){
        String decrpytedFilePath = "files/ciphertext.txt";
        String outputPlainTextFilePath = "files/afterDecrpy.txt";

        try {
            decryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            decryptProcess(new FileInputStream(decrpytedFilePath), new FileOutputStream(outputPlainTextFilePath));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void decryptProcess(InputStream input, OutputStream output) {
        output = new CipherOutputStream(output, decryptionCipher);
        try {
            writeBytes(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createSecretKey(String key){
        byte[] keyBytes = key.getBytes();

        System.out.println("total byte bit: " + keyBytes.length * 8);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("DES");
            secretKey = factory.generateSecret(new DESKeySpec(keyBytes));

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(InvalidKeyException e){
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public void encryptTheFile(){
        String plaintextFilePath = "files/plainText.txt";
        String outputFilePath = "files/ciphertext.txt";
        try {
            encryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            encryptProcess(new FileInputStream(plaintextFilePath), new FileOutputStream(outputFilePath));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void encryptProcess(InputStream input, OutputStream output){
        output = new CipherOutputStream(output, encryptionCipher);
        try {
            writeBytes(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBytes(InputStream input, OutputStream output) throws IOException {
        byte[] writeBuffer = new byte[512];
        int readBytes;

        while ((readBytes = input.read(writeBuffer)) >= 0) {
            output.write(writeBuffer, 0, readBytes);
        }

        output.close();
        input.close();
    }

}
