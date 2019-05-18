package signature;
import bignumber_generator.BigNumberGenerator;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;
import sha_hash.SHAHash;

import  java.math.BigInteger;
import  java.security.KeyPair;
import  java.security.KeyPairGenerator;
import  java.security.NoSuchAlgorithmException;
import  java.security.interfaces.DSAParams;
import  java.security.interfaces.DSAPrivateKey;
import  java.security.interfaces.DSAPublicKey;


public class DSASignature {
    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;

    private BigInteger number_p;
    private BigInteger number_q;
    private BigInteger number_g;
    private BigInteger number_x;
    private BigInteger number_y;

    private BigInteger[] signParameter;
    private BigInteger[] publicParameter;

    public DSASignature(){
        bigNumberGenerator = new BigNumberGenerator();
        fastExponentiation = new FastExponentiationImpl();
    }

    // getter for signParameter
    public BigInteger[] getSignParameter(){
        return signParameter;
    }

    // getter for public key parameter
    public BigInteger[] getPublicParameter(){
        return publicParameter;
    }

    /**
     * Generates p,q,g,x(private key) and y(public key)
     * To generate p,q and g, I used the java.security library
     * Then generate x and y
     * After all, create public key parameter
     */
    public void keyGeneration() {
        KeyPairGenerator keyGen = null;

        try {
            keyGen = KeyPairGenerator.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.genKeyPair();
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();

        DSAParams dsaParams = privateKey.getParams();
        number_p = dsaParams.getP();
        number_q = dsaParams.getQ();
        number_g = dsaParams.getG();


        number_x = computePrivateKey();
        number_y = computePublicKey();
        createPublicParameters();
    }

    /**
     * Creates an array for public parameters, includes:
     * p, q, g and y(public key)
     */
    private void createPublicParameters() {
        publicParameter = new BigInteger[4];
        publicParameter[0] = number_p;
        publicParameter[1] = number_q;
        publicParameter[2] = number_g;
        publicParameter[3] = number_y;
    }

    /**
     * Compute private key, invoke generateBigIntegersWithNumberOfBits in the BigNumberGenerator
     * @return private key as big integer
     */
    private BigInteger computePrivateKey() {
        return bigNumberGenerator.generateBigIntegersWithNumberOfBits(number_p.bitLength());
    }

    /**
     * Compute public key via fast exponentiation method
     * @return public key as big integer
     */
    private BigInteger computePublicKey(){
        return fastExponentiation.calculateModularWithFastExponentiation(number_g, number_x, number_p);
    }

    /**
     * Sign the message
     * First hash the message then convert to the big integer
     * Generate random number_k and signature parameter
     * after all, create signature parameter
     * @param message
     */
    public void signMessage(String message) {
        byte[] hashMessageWithByte = SHAHash.sha3_256_hash_with(message);
        BigInteger hashedBigInteger = new BigInteger(hashMessageWithByte);
        BigInteger number_k = generateRandomNumber_k();
        BigInteger number_r = computeNumber_r(number_k);
        BigInteger number_s = computeNumber_s(number_k, number_r, hashedBigInteger);
        createSignParameter(number_r, number_s);

    }

    /**
     * Generates random number k with (k < q), invoke
     *          generateBigIntegersWithNumberOfBits in BigNumberGenerator
     * @return random number k as a big integer
     */
    private BigInteger generateRandomNumber_k(){
        return bigNumberGenerator.generateBigIntegersWithNumberOfBits(number_q.bitLength());
    }

    /**
     * Compute the signature parameter r
     * @param number_k
     * @return
     */
    private BigInteger computeNumber_r(BigInteger number_k){
        // g.modPow(k, p)
        BigInteger innerOperation = fastExponentiation.calculateModularWithFastExponentiation(number_g, number_k, number_p);
        // g.modPow(k, p).mod(q);
        BigInteger result = innerOperation.mod(number_q);
        return result;
    }

    /**
     * Compute the signature parameter s
     * @param number_k
     * @param number_r
     * @param hash
     * @return
     */
    private BigInteger computeNumber_s(BigInteger number_k, BigInteger number_r, BigInteger hash){
        BigInteger inverse_k = number_k.modInverse(number_q);
        // x.r
        BigInteger x_r = number_x.multiply(number_r);
        // hash + x.r
        BigInteger hash_x_r = hash.add(x_r);
        //k^{-1}*(H(M)+ xr)
        BigInteger k_hash_x_r = inverse_k.multiply(hash_x_r);
        return k_hash_x_r.mod(number_q);

    }

    /**
     * Create an array for signature parameters, include: r and s
     * @param number_r
     * @param number_s
     */
    private void createSignParameter(BigInteger number_r, BigInteger number_s){
        signParameter = new BigInteger[2];
        signParameter[0] = number_r;
        signParameter[1] = number_s;
    }

    /**
     * Verify the message with sender's signature parameter and public parameter
     * If the message is verified => print "Verified" otherwise "Not Verified"
     * @param message will be verified
     * @param signParameter sender's signature parameter
     * @param publicParameter sender's public parameter
     */
    public void verifyMessage(String message, BigInteger[] signParameter, BigInteger[] publicParameter){
        BigInteger number_r = signParameter[0];
        BigInteger number_s = signParameter[1];

        BigInteger number_p = publicParameter[0];
        BigInteger number_q = publicParameter[1];
        BigInteger number_g = publicParameter[2];
        BigInteger number_y = publicParameter[3];

        byte[] hashMessageWithByte = SHAHash.sha3_256_hash_with(message);
        BigInteger hashedBigInteger = new BigInteger(hashMessageWithByte);

        BigInteger number_w = computeNumber_w(number_s, number_q);
        BigInteger number_u1 = computeNumber_u1(number_w, number_q, hashedBigInteger);
        BigInteger number_u2 = computeNumber_u2(number_r, number_w, number_q);
        BigInteger number_v = computeNumber_v(number_g, number_u1, number_u2, number_p, number_q, number_y);

        if (number_v.compareTo(number_r) == 0){
            System.out.println("Verified");
        }else{
            System.out.println("Not Verified");
        }
    }

    /**
     * Compute number w for verifying
     * @param number_s
     * @param number_q
     * @return
     */
    private BigInteger computeNumber_w(BigInteger number_s, BigInteger number_q){
        return number_s.modInverse(number_q);
    }

    /**
     * Compute number u1 for verifying
     * @param number_w
     * @param number_q
     * @param hash
     * @return
     */
    private BigInteger computeNumber_u1(BigInteger number_w, BigInteger number_q, BigInteger hash){
        // hash.multiply(w)
        BigInteger hash_multiply_w = hash.multiply(number_w);
        // hash.multiply(w).mod(q);
        BigInteger result = hash_multiply_w.mod(number_q);
        return result;
    }

    /**
     * Compute number u2 for verifying
     * @param number_r
     * @param number_w
     * @param number_q
     * @return
     */
    private BigInteger computeNumber_u2(BigInteger number_r, BigInteger number_w, BigInteger number_q){
        //  r.multiply(w)
        BigInteger r_multiply_w = number_r.multiply(number_w);
        // r.multiply(w).mod(q);
        BigInteger result = r_multiply_w.mod(number_q);
        return result;
    }

    /**
     * Compute number v for verifying
     * @param number_g
     * @param number_u1
     * @param number_u2
     * @param number_p
     * @param number_q
     * @param number_y
     * @return
     */
    private BigInteger computeNumber_v(BigInteger number_g, BigInteger number_u1, BigInteger number_u2, BigInteger number_p, BigInteger number_q, BigInteger number_y){
        // g^{u1}
        BigInteger g_to_u1 = number_g.modPow(number_u1, number_p);
        // y^{u2}
        BigInteger y_to_u2 = number_y.modPow(number_u2, number_p);
        // g^{u1} * y^{u2}
        BigInteger multiply = g_to_u1.multiply(y_to_u2);
        // (g^{u1} * y^{u2}).mod(p)
        BigInteger multiply_mod = multiply.mod(number_p);
        // [(g^{u1} * y^{u2}).mod(p)].mod q
        BigInteger result = multiply_mod.mod(number_q);
        return result;
    }
}
