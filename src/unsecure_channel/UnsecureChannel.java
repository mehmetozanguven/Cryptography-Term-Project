package unsecure_channel;

import bignumber_generator.BigNumberGenerator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Unsecure channel representation class
 *
 */
public class UnsecureChannel implements DiffieHellmanUnsecureChannel, RSAUnsecureChannel, DSSUnsecureChannel{
    // DiffieHellman attributes
    private BigInteger primeNum_p;
    private BigInteger anyNum_alpha;
    private BigInteger diffieHellmanSenderPublicKey;
    private BigInteger diffieHellmanReceiverPublicKey;
    // DiffieHellman attributes

    // RSA attributes
    private Map<BigInteger, BigInteger> senderPublicKey;
    private Map<BigInteger, BigInteger> receiverPublicKey;
    // RSA attributes

    // DSS attributes
    private Map<Integer, BigInteger> senderPublicKeyComponents;
    private Map<Integer, BigInteger> senderSignaturePair;
    private Map<Integer, BigInteger> receiverPublicKeyComponents;
    private Map<Integer, BigInteger> receiverSignaturePair;
    // DSS attributes

    private BigNumberGenerator bigNumberGenerator;
    private BigInteger senderRandomDESWithRSAEncryption;
    private BigInteger receiverRandomDESWithRSAEncryption;

    public UnsecureChannel(){
        bigNumberGenerator = new BigNumberGenerator();
        senderPublicKey = new HashMap<>();
        receiverPublicKey = new HashMap<>();
    }


    // ------------------------(START) DiffieHellmanUnsecureChannel Implementation ---------------------------------

    /**
     * Diffie–Hellman Set-up
     *  1. Choose a large prime p.
     *  2. Choose an integer α ∈ {2, 3, . . . , p − 2}.
     */
    public void diffieHellmanSetup(){
        generateBigIntegerPrimeNumber();
        generateNumberAlpha();
    }

    /**
     * Generate big prime integer
     * To decide whether big integer is prime or not, I am using Fermat's Little Theorem
     * In fermat's little theorem:
     *      a^{p-1} = 1 (mod p) where p is the primeNumCandidate in the code.
     */
    private void generateBigIntegerPrimeNumber(){
        primeNum_p = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(64);
    }

    /**
     * Choose number alpha (a) from the {... p-2} where p is the prime number
     */
    private void generateNumberAlpha(){
        BigInteger primeNum_minus2 = primeNum_p.subtract(BigInteger.valueOf(2));
        anyNum_alpha = bigNumberGenerator.generateBigIntegersWithNumberOfBits(primeNum_minus2.bitLength());
    }

    public BigInteger getPrimeNum_p() {
        return primeNum_p;
    }

    public BigInteger getAnyNum_alpha() {
        return anyNum_alpha;
    }

    public BigInteger getDiffieHellmanSenderPublicKey() {
        return diffieHellmanSenderPublicKey;
    }

    public void setDiffieHellmanSenderPublicKey(BigInteger diffieHellmanSenderPublicKey) {
        this.diffieHellmanSenderPublicKey = diffieHellmanSenderPublicKey;
    }

    public BigInteger getDiffieHellmanReceiverPublicKey() {
        return diffieHellmanReceiverPublicKey;
    }

    public void setDiffieHellmanReceiverPublicKey(BigInteger diffieHellmanReceiverPublicKey) {
        this.diffieHellmanReceiverPublicKey = diffieHellmanReceiverPublicKey;
    }

    // ------------------------(END) DiffieHellmanUnsecureChannel Implementation ---------------------------------



    // ------------------------(START) RSAUnsecureChannel Implementation ---------------------------------

    /**
     * Returns the rsa public key and public number n
     * @return
     */
    public Map<BigInteger, BigInteger> getSenderPublicKey() {
        return senderPublicKey;
    }

    /**
     * set the sender public key and large prime number n as a hashmap
     * where key=n and value is the public key
     * @param senderPublicKey
     */
    public void setSenderPublicKeyPair(Map<BigInteger, BigInteger> senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    /**
     * Returns the receiver public key
     * @return
     */
    public Map<BigInteger, BigInteger> getReceiverPublicKey() {
        return receiverPublicKey;
    }

    /**
     * set the receiver public key and large prime number n as a hashmap
     * where key=n and value is the public key
     * @param receiverPublicKey
     */
    public void setReceiverPublicKeyPair(Map<BigInteger, BigInteger> receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public BigInteger getSenderRandomDESWithRSAEncryption() {
        return senderRandomDESWithRSAEncryption;
    }

    public void setSenderRandomDESWithRSAEncryption(BigInteger senderRandomDESWithRSAEncryption) {
        this.senderRandomDESWithRSAEncryption = senderRandomDESWithRSAEncryption;
    }

    public BigInteger getReceiverRandomDESWithRSAEncryption() {
        return receiverRandomDESWithRSAEncryption;
    }

    public void setReceiverRandomDESWithRSAEncryption(BigInteger receiverRandomDESWithRSAEncryption) {
        this.receiverRandomDESWithRSAEncryption = receiverRandomDESWithRSAEncryption;
    }

    // ------------------------(END) RSAUnsecureChannel Implementation ---------------------------------


    // ------------------------(START) DSSUnsecureChannel Implementation ---------------------------------

    @Override
    public Map<Integer, BigInteger> getSenderPublicKeyComponents() {
        if (this.senderPublicKeyComponents == null){
            throw new NullPointerException("Sender Key Components null. First invoke setSenderPublicKeyComponents()");
        }else{
            return this.senderPublicKeyComponents;
        }
    }

    @Override
    public void setSenderPublicKeyComponents(Map<Integer, BigInteger> senderPublicKeyComponents) {
        this.senderPublicKeyComponents = senderPublicKeyComponents;
    }

    @Override
    public Map<Integer, BigInteger> getSenderSignaturePair() {
        if (this.senderSignaturePair == null){
            throw new NullPointerException("Sender Signature pair is null. First invoke setSenderSignaturePair()");
        }else{
            return this.receiverPublicKeyComponents;
        }
    }

    @Override
    public void setSenderSignaturePair(Map<Integer, BigInteger> senderSignaturePair) {
        this.senderSignaturePair = senderSignaturePair;
    }

    @Override
    public Map<Integer, BigInteger> getReceiverPublicKeyComponents() {
        if (this.receiverPublicKeyComponents == null){
            throw new NullPointerException("Receiver Key Components null. First invoke setSenderPublicKeyComponents()");
        }else{
            return this.receiverPublicKeyComponents;
        }
    }

    @Override
    public void setReceiverPublicKeyComponents(Map<Integer, BigInteger> receiverPublicKeyComponents) {
        this.receiverPublicKeyComponents = receiverPublicKeyComponents;
    }

    @Override
    public Map<Integer, BigInteger> getReceiverSignaturePair() {
        if (this.receiverSignaturePair == null){
            throw new NullPointerException("Receiver Signature pair is null. First invoke setSenderSignaturePair()");
        }else{
            return this.receiverPublicKeyComponents;
        }
    }

    @Override
    public void setReceiverSignaturePair(Map<Integer, BigInteger> receiverSignaturePair) {
        this.receiverSignaturePair = receiverSignaturePair;
    }
}
