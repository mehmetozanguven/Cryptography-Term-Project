package encryption.des;

import measurement.Measure;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class DESEncryption {
    private final String ONE_PAGE_PLAINTEXT_PATH = "files/onePageLength/onePageLength.txt";
    private final String ONE_PAGE_CIPHERTEXT_OUTPUT_PATH = "files/onePageLength/onePageLengthCipherText.txt";
    private final String ONE_PAGE_DECRYPTED_PATH = "files/onePageLength/onePageLengthDecrypted.text.txt";

    private final String TEN_PAGE_PLAINTEXT_PATH = "files/tenPageLength/tenPageLength.txt";
    private final String TEN_PAGE_CIPHERTEXT_OUTPUT_PATH = "files/tenPageLength/tenPageLengthCipherText.txt";
    private final String TEN_PAGE_DECRYPTED_PATH = "files/tenPageLength/tenPageLengthDecryptedText.txt";

    private final String HUNDRED_PAGE_PLAINTEXT_PATH = "files/hundredPageLength/hundredPageLength.txt";
    private final String HUNDRED_PAGE_CIPHERTEXT_OUTPUT_PATH = "files/hundredPageLength/hundredPageLengthCipherText.txt";
    private final String HUNDRED_PAGE_DECRYPTED_PATH = "files/hundredPageLength/hundredPageLengthDecryptedText.txt";

    private final String THOUSAND_PAGE_PLAINTEXT_PATH = "files/thousandPageLength/thousandPageLength.txt";
    private final String THOUSAND_PAGE_CIPHERTEXT_OUTPUT_PATH = "files/thousandPageLength/thousandPageLengthCiphertext.txt";
    private final String THOUSAND_PAGE_DECRYPTED_PATH = "files/thousandPageLength/thousandPageLengthDecryptedText.txt";

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
            encrypt_page_length(ONE_PAGE_PLAINTEXT_PATH, ONE_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else if (pageType == 1)
            encrypt_page_length(TEN_PAGE_PLAINTEXT_PATH, TEN_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else if (pageType == 2)
            encrypt_page_length(HUNDRED_PAGE_PLAINTEXT_PATH, HUNDRED_PAGE_CIPHERTEXT_OUTPUT_PATH);
        else
            encrypt_page_length(THOUSAND_PAGE_PLAINTEXT_PATH, THOUSAND_PAGE_CIPHERTEXT_OUTPUT_PATH);

    }

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

//    private void encrypt_10_page_length() {
//        String tenPagePlaintextFilePath = "files/tenPageLength/tenPageLength.txt";
//        String cipherTextOutput = "files/tenPageLength/tenPageLengthCipherText.txt";
//
//        try {
//            encryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
//            encryptProcess(new FileInputStream(tenPagePlaintextFilePath), new FileOutputStream(cipherTextOutput));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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
            decrypt_page_length(ONE_PAGE_CIPHERTEXT_OUTPUT_PATH, ONE_PAGE_DECRYPTED_PATH);
        else if (pageType == 1)
            decrypt_page_length(TEN_PAGE_CIPHERTEXT_OUTPUT_PATH, TEN_PAGE_DECRYPTED_PATH);
        else if (pageType == 2)
            decrypt_page_length(HUNDRED_PAGE_CIPHERTEXT_OUTPUT_PATH, HUNDRED_PAGE_DECRYPTED_PATH);
        else
            decrypt_page_length(THOUSAND_PAGE_CIPHERTEXT_OUTPUT_PATH, THOUSAND_PAGE_DECRYPTED_PATH);

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

//    private void decrypt_1_page_length() {
//
//    }
//
//    private void decrypt_10_page_length() {
//        String tenPageCipherTextFilePath = "files/tenPageLength/tenPageLengthCipherText.txt";
//        String decryptedFileOutputPath = "files/tenPageLength/tenPageLengthDecryptedText.txt";
//
//        try {
//            decryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
//            decryptProcess(new FileInputStream(tenPageCipherTextFilePath), new FileOutputStream(decryptedFileOutputPath));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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
