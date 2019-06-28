package person.rsa_person;

import bignumber_generator.BigNumberGenerator;
import encryption.des.DESEncryption;
import encryption.rsa.RSAEncryption;
import file_handler.FilePathHandler;
import file_handler.FileToStringConverter;
import greatest_common_divisor.GreatestCommonDivisorCalculator;
import signature.DSASignature;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for Sender and receiver
 */
public abstract class Person {
    private int bitLength;
    private BigInteger largePrimeNumber_p;
    private BigInteger largePrimeNumber_q;
    private BigInteger computeNumber_n;
    private BigInteger eulerPhiResult;
    private BigInteger randomDESKey;
    private BigInteger randomDESKeyWithRSAEncryption;

    // this map private fields represents the key pair (N,e) & (N,d) respectively
    private Map<BigInteger, BigInteger> publicKeyPair;
    private Map<BigInteger, BigInteger> privateKeyPair;

    // DSA attributes
    private String fileAsString;
    private signature.DSASignature DSASignature;
    private BigInteger[] signParameter;
    private BigInteger[] publicParameter;

    private BigNumberGenerator bigNumberGenerator;

    public Person(){
        this.bigNumberGenerator = new BigNumberGenerator();
        this.DSASignature = new DSASignature();
    }

    public void setBitLength(int bitLength) {
        this.bitLength = bitLength;
    }

    /**
     * Generates large prime number p for RSA
     */
    public void generateLargePrimeNumber_p(){
        largePrimeNumber_p = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(bitLength);
    }

    /**
     * Generates large prime number q for RSA
     */
    public void generateLargePrimeNumber_q(){
        largePrimeNumber_q = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(bitLength);
    }

    /**
     * Compute n = p.q
     * Don't invoke this method until generate p & q
     */
    public void compute_number_n(){
        computeNumber_n = largePrimeNumber_p.multiply(largePrimeNumber_q);
    }

    /**
     * Compute the euler phi function T = (p-1) * (q-1)
     * Don't invoke this method until generate p & q
     */
    public void computeEulerPhiFunction(){
        eulerPhiResult = largePrimeNumber_p.subtract(BigInteger.ONE).multiply(
                largePrimeNumber_q.subtract(BigInteger.ONE)
        );
    }

    /**
     * Compute the public key pair which is N(compute_n), e
     * Don't invoke this method until compute euler phi function
     * Note that: gcd(e, T) = 1 (must be!!)
     */
    public void computePublicKey(){
        publicKeyPair = new HashMap<>();
        BigInteger e = bigNumberGenerator.generateBigIntegersWithNumberOfBits(bitLength);
        boolean isNumber_e_found = false;

        while (!isNumber_e_found){
            BigInteger result = GreatestCommonDivisorCalculator.calculateGreatestCommonDivisor(e, eulerPhiResult);
            if (result.compareTo(BigInteger.ONE)== 0){
                isNumber_e_found = true;
            }else{
                e = e.add(BigInteger.ONE);
            }
        }

        publicKeyPair.put(computeNumber_n, e);
    }

    /**
     * Compute the private key pair which is N(compute_n), d
     * Don't invoke this method until compute public key calling
     * Note that: d.e = 1 (mod T-eulerPhi-) (must be !!!)
     */
    public void computePrivateKey(){
        privateKeyPair = new HashMap<>();
        BigInteger e = publicKeyPair.get(computeNumber_n);
        BigInteger d = e.modInverse(eulerPhiResult);
        privateKeyPair.put(computeNumber_n, d);
    }

    /**
     * Generates random des key,
     * this key will be used to encrypt the files
     */
    public void generateRandomDESKey(){
        this.randomDESKey = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(bitLength);
    }

    /**
     * Encrypt the random des with rsa encryption
     * Don't invoke this method, before calling generateRandomDESKey(), if you call it @throws an null pointer ex.
     * rsaEncryption.encryptTheKey(..) will return the encrypted number
     * @param publicKeyPairs key pair to encrypt the des key
     * @param rsaEncryption
     */
    public void encryptRandomDESKeyWithRSA(Map publicKeyPairs, RSAEncryption rsaEncryption){
        this.randomDESKeyWithRSAEncryption = rsaEncryption.encryptTheKey(publicKeyPairs, this.randomDESKey);
    }

    public Map getPublicKeyPair(){
        return publicKeyPair;
    }

    public BigInteger getRandomDESKeyWithRSAEncryption(){
        return this.randomDESKeyWithRSAEncryption;
    }


    /**
     * Encrypts the files according to the types(pageType)
     * @param pageType states the page length (0 => 1 page-length, 1 => 10 page-length ...)
     * @param desEncryption
     */
    public void encryptFile_publish(int pageType, DESEncryption desEncryption){
        desEncryption.createSecretKey(randomDESKey.toString());
        desEncryption.encryptTheFile(pageType);
    }

    /**
     * Decrypts the files according to the types(pageType)
     * First decrypt the "randomDESKeyWithRSAEncryption" using private key pair
     * then decrypt the all files according to the "decryptDESKey"
     * @param pageType states the page length (0 => 1 page-length, 1 => 10 page-length ...)
     * @param rsaEncryption represents the rsa encryption class
     * @param randomDESKeyWithRSAEncryption encrypted des key
     * @param desEncryption represents the des encryption class
     */
    public void decryptFile(int pageType, RSAEncryption rsaEncryption, BigInteger randomDESKeyWithRSAEncryption, DESEncryption desEncryption){
        BigInteger decryptDESKey = rsaEncryption.decryptTheKey(privateKeyPair, randomDESKeyWithRSAEncryption);
        desEncryption.createSecretKey(decryptDESKey.toString());
        desEncryption.decryptTheFile(pageType);
    }


    // ----------------------- DSA Methods --------------------------------
    /**
     * Generates essential keys for DSA algorithm
     * To generate keys, invoke the keyGeneration method in DSASignature class
     * Then get the public parameter as an array
     */
    public void DSAKeyGeneration(){
        DSASignature.keyGeneration();
        publicParameter = DSASignature.getPublicParameter();
    }

    /**
     * Sign the file
     * @param fileType represents the file type.
     *                 0 => file which is one page length
     *                 1 =>               ten page length
     *                 2 =>               hundred page length
     *                 others =>          thousand page length
     *
     *  After learning file type, get the file content as string
     *  then sign that string(file content) with invoking signMessage method in DSASignature
     *  after all, get the signature parameter(r,s) as an array
     */
    public void signFile(int fileType){
        if (fileType == 0)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.ONE_PAGE_PLAINTEXT_PATH);
        else if (fileType == 1)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.TEN_PAGE_PLAINTEXT_PATH);
        else if (fileType == 2)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.HUNDRED_PAGE_PLAINTEXT_PATH);
        else
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.THOUSAND_PAGE_PLAINTEXT_PATH);

        DSASignature.signMessage(fileAsString);
        signParameter =  DSASignature.getSignParameter();
    }

    /**
     * @return signature parameter
     */
    public BigInteger[] getSignParameter(){
        return signParameter;
    }

    /**
     * @return public key parameter
     */
    public BigInteger[] getPublicParameter(){
        return publicParameter;
    }

    /**
     * @return file content as string
     */
    public String getFileAsString(){
        return fileAsString;
    }

    /**
     * Verify the encrypted file
     * First decrypt it, then get content of decrypted file
     * and verify it.
     * @param pageType represents page type (1 page-length, 10 page-length...)
     * @param rsaEncryption rsa encryption object
     * @param desEncryption des encryption object
     * @param signParameter is the sender's signature parameter
     * @param publicParameter is the sender's receiver public key parameter
     * invoke the method verifyFile in DSASignature
     */
    public void verifyFile(int pageType,
                           BigInteger[] signParameter,
                           BigInteger[] publicParameter,
                           RSAEncryption rsaEncryption,
                           DESEncryption desEncryption,
                           BigInteger randomDESKeyWithRSAEncryption){

        decryptFile(pageType, rsaEncryption, randomDESKeyWithRSAEncryption, desEncryption);

        String message = getDecrpytedFileContentAsString(pageType);


        DSASignature.verifyMessage(message, signParameter, publicParameter);
    }

    private String getDecrpytedFileContentAsString(int fileType) {
        String fileContent = "";
        if (fileType == 0)
            fileContent = FileToStringConverter.getFileContentAsString(FilePathHandler.ONE_PAGE_DECRYPTED_PATH);
        else if (fileType == 1)
            fileContent = FileToStringConverter.getFileContentAsString(FilePathHandler.TEN_PAGE_DECRYPTED_PATH);
        else if (fileType == 2)
            fileContent = FileToStringConverter.getFileContentAsString(FilePathHandler.HUNDRED_PAGE_DECRYPTED_PATH);
        else
            fileContent = FileToStringConverter.getFileContentAsString(FilePathHandler.THOUSAND_PAGE_DECRYPTED_PATH);
        return fileContent;

    }


}
