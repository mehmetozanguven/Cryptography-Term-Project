package person;

import encryption.des.DESEncryption;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;

import java.math.BigInteger;
import java.util.Random;

public abstract class Person {

    private BigInteger primeNumber_p_from_unsecureChannel;
    private BigInteger anyNumber_alpha_from_unsecureChannel;
    private BigInteger privateKey;
    private BigInteger publicKey;

    private DESEncryption desEncryption;
    private FastExponentiation fastExponentiation;

    public Person(BigInteger primeNumber_p_from_unsecureChannel, BigInteger anyNumber_alpha_from_unsecureChannel){
        this.primeNumber_p_from_unsecureChannel = primeNumber_p_from_unsecureChannel;
        this.anyNumber_alpha_from_unsecureChannel = anyNumber_alpha_from_unsecureChannel;

        this.desEncryption = new DESEncryption();
        this.fastExponentiation = new FastExponentiationImpl();

    }

    public void generatePrivateKey() {
        // TODO: Generate random big integers in the range {2, 3, ..., p-2} where p = primeNumber_p_from_unsecureCha
        BigInteger primeNum_minus2 = primeNumber_p_from_unsecureChannel.subtract(BigInteger.valueOf(2));
        Random rnd = new Random();
        BigInteger alphaNumCandidate = new BigInteger(primeNum_minus2.bitLength(), rnd);
        privateKey = alphaNumCandidate;
    }

    public void generatePublicKey() {
        publicKey = fastExponentiation.calculateModularWithFastExponentiation(anyNumber_alpha_from_unsecureChannel, privateKey, primeNumber_p_from_unsecureChannel);
    }

    public BigInteger publishPublicKey(){
        return this.publicKey;
    }

    public void encryptFile_publish(BigInteger receiverPublicKey, int pageType){
        BigInteger secretKey = fastExponentiation.calculateModularWithFastExponentiation(receiverPublicKey,privateKey, primeNumber_p_from_unsecureChannel);
        desEncryption.createSecretKey(secretKey.toString());
        desEncryption.encryptTheFile(pageType);
    }

    public void decryptFile(BigInteger senderPublicKey, int pageType){
        BigInteger secretKey = fastExponentiation.calculateModularWithFastExponentiation(senderPublicKey,privateKey, primeNumber_p_from_unsecureChannel);
        desEncryption.createSecretKey(secretKey.toString());
        desEncryption.decryptTheFile(pageType);
    }

}
