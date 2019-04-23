package encryption.des;

import file_path_handler.FilePathHandler;
import measurement.Measure;

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
    private Measure measure;

    /**
     * Creates des secret key
     * Get the key and convert to the bytes array
     * Then get instance of des secret kay factory and generate the secret key
     * according to the key's bytes array
     * @param key is the key for the secret key
     */
    public void createSecretKey(String key){
        byte[] keyBytes = key.getBytes();

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

    /**
     * Encrypts the file according to the page type
     * int Page type states the page-length
     * for 0 => Encrypts the 1 page - length file
     *     1 => Encrypts the 10 page - length file
     *     2 => Encrypts the 100 page - length file
     *     3 => Encrypts the 1000 page - length file
     * @param pageType
     */
    public void encryptTheFile(int pageType){
        if (pageType == 0)
            encrypt_page_length(FilePathHandler.ONE_PAGE_PLAINTEXT_PATH, FilePathHandler.ONE_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else if (pageType == 1)
            encrypt_page_length(FilePathHandler.TEN_PAGE_PLAINTEXT_PATH, FilePathHandler.TEN_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else if (pageType == 2)
            encrypt_page_length(FilePathHandler.HUNDRED_PAGE_PLAINTEXT_PATH, FilePathHandler.HUNDRED_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else
            encrypt_page_length(FilePathHandler.THOUSAND_PAGE_PLAINTEXT_PATH, FilePathHandler.THOUSAND_PAGE_CIPHERTEXT_OUTPUT_PATH);

    }

    /**
     * Prepare the encryptionCipher, DES mode, encrypt_mode, secret key etc..
     * @param plaintextPath is the plain text path
     * @param cipherTextOutputPath will be the encrypted output text
     */
    private void encrypt_page_length(String plaintextPath, String cipherTextOutputPath) {

        try {
            encryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            encryptProcess(new FileInputStream(plaintextPath), new FileOutputStream(cipherTextOutputPath));
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

    /**
     * Encrypt the plaintext
     * Before starting the ecryption process, stamp the time and memory usage
     * After process is done, stamp the time and memory usage again to see performance
     * @param input is the plaintext input stream
     * @param output is the ciphertext output stream
     */
    private void encryptProcess(InputStream input, OutputStream output){
        measure = Measure.getInstance();

        long executionStartTime = System.nanoTime();
        long beforeUsedMemory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        output = new CipherOutputStream(output, encryptionCipher);
        try {
            writeBytes(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long executionEndTime = System.nanoTime();
        long afterUsedMemory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        measure.setExecutionStartTime(executionStartTime);
        measure.setExecutionEndTime(executionEndTime);
        measure.setAfterUsedMemory(afterUsedMemory);
        measure.setBeforeUsedMemory(beforeUsedMemory);
    }

    /**
     * After encryption/decryption process, write the encrypted/decrypted text to the correspond file
     * @param input is the plaintext input stream
     * @param output is the ciphertext output stream
     * @throws IOException
     */
    private static void writeBytes(InputStream input, OutputStream output) throws IOException {
        byte[] writeBuffer = new byte[512];
        int readBytes;

        while ((readBytes = input.read(writeBuffer)) >= 0) {
            output.write(writeBuffer, 0, readBytes);
        }

        output.close();
        input.close();
    }

    public void decryptTheFile(int pageType){
        if (pageType == 0)
            decrypt_page_length(FilePathHandler.ONE_PAGE_CIPHERTEXT_OUTPUT_PATH, FilePathHandler.ONE_PAGE_DECRYPTED_PATH);
        else if (pageType == 1)
            decrypt_page_length(FilePathHandler.TEN_PAGE_CIPHERTEXT_OUTPUT_PATH, FilePathHandler.TEN_PAGE_DECRYPTED_PATH);
        else if (pageType == 2)
            decrypt_page_length(FilePathHandler.HUNDRED_PAGE_CIPHERTEXT_OUTPUT_PATH, FilePathHandler.HUNDRED_PAGE_DECRYPTED_PATH);
        else
            decrypt_page_length(FilePathHandler.THOUSAND_PAGE_CIPHERTEXT_OUTPUT_PATH, FilePathHandler.THOUSAND_PAGE_DECRYPTED_PATH);

    }

    private void decrypt_page_length(String cipherTextPath, String decryptedTextPath) {

        try {
            decryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            decryptProcess(new FileInputStream(cipherTextPath), new FileOutputStream(decryptedTextPath));
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
        measure = Measure.getInstance();

        long executionStartTime = System.nanoTime();
        long beforeUsedMemory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        output = new CipherOutputStream(output, decryptionCipher);
        try {
            writeBytes(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long executionEndTime = System.nanoTime();
        long afterUsedMemory=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        measure.setExecutionStartTime(executionStartTime);
        measure.setExecutionEndTime(executionEndTime);
        measure.setAfterUsedMemory(afterUsedMemory);
        measure.setBeforeUsedMemory(beforeUsedMemory);
    }

}
