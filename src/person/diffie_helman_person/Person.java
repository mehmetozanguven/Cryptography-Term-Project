package person.diffie_helman_person;

import encryption.des.DESEncryption;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;

import java.math.BigInteger;
import java.util.Random;

/**
 * Abstract class for Sender and receiver
 */
public abstract class Person  {

    private BigInteger primeNumber_p_from_unsecureChannel;
    private BigInteger anyNumber_alpha_from_unsecureChannel;
    private BigInteger privateKey;
    private BigInteger publicKey;

    private DESEncryption desEncryption;
    private FastExponentiation fastExponentiation;


    public Person(){
        this.desEncryption = new DESEncryption();
        this.fastExponentiation = new FastExponentiationImpl();
    }

    /**
     * Private key generation method
     * In this method, person will choose random number in the range {... p - 2}
     * where p=primeNumber_p_from_unsecureChannel
     */
    public void generatePrivateKey() {
        // TODO: Generate random big integers in the range {2, 3, ..., p-2} where p = primeNumber_p_from_unsecureCha
        BigInteger primeNum_minus2 = primeNumber_p_from_unsecureChannel.subtract(BigInteger.valueOf(2));
        Random rnd = new Random();
        BigInteger alphaNumCandidate = new BigInteger(primeNum_minus2.bitLength(), rnd);
        privateKey = alphaNumCandidate;
    }

    /**
     * Public key generation method
     * public key will be generated by "alpha^(privateKey)mod p", where p=primeNumber_p_from_unsecureChannel
     * To calculate this modulo operation, I am using fast exponentiation method
     */
    public void generatePublicKey() {
        publicKey = fastExponentiation.calculateModularWithFastExponentiation(anyNumber_alpha_from_unsecureChannel, privateKey, primeNumber_p_from_unsecureChannel);
    }

    /**
     * Person will publish his/her private key.
     * @return
     */
    public BigInteger publishPublicKey(){
        return this.publicKey;
    }

    /**
     * According to the receiver public key, sender will encrypt the file
     * @param receiverPublicKey is the receiver public key
     * @param pageType states the page length (0 => 1 page-length, 1 => 10 page-length ...)
     */
    public void encryptFile_publish(BigInteger receiverPublicKey, int pageType){
        BigInteger secretKey = fastExponentiation.calculateModularWithFastExponentiation(receiverPublicKey, privateKey, primeNumber_p_from_unsecureChannel);
        desEncryption.createSecretKey(secretKey.toString());
        desEncryption.encryptTheFile(pageType);
    }

    /**
     * According to the sender public key, receiver will decrypt the file
     * @param senderPublicKey is the sender public key
     * @param pageType states the page length (0 => 1 page-length, 1 => 10 page-length ...)
     */
    public void decryptFile(BigInteger senderPublicKey, int pageType){
        BigInteger secretKey = fastExponentiation.calculateModularWithFastExponentiation(senderPublicKey,privateKey, primeNumber_p_from_unsecureChannel);
        desEncryption.createSecretKey(secretKey.toString());
        desEncryption.decryptTheFile(pageType);
    }

    public void setPrimeNumber_p_from_unsecureChannel(BigInteger primeNumber_p_from_unsecureChannel) {
        this.primeNumber_p_from_unsecureChannel = primeNumber_p_from_unsecureChannel;
    }

    public void setAnyNumber_alpha_from_unsecureChannel(BigInteger anyNumber_alpha_from_unsecureChannel) {
        this.anyNumber_alpha_from_unsecureChannel = anyNumber_alpha_from_unsecureChannel;
    }
}
